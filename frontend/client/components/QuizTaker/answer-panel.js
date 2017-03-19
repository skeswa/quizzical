
import FontIcon from 'material-ui/FontIcon'
import classNames from 'classnames'
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component, PropTypes } from 'react'

import style from './answer-panel.css'
import FreeResponseAnswerer from 'components/FreeResponseAnswerer'
import MultipleChoiceAnswerer from 'components/MultipleChoiceAnswerer'

const FREE_RESPONSE_BLANK_ANSWER = '    '

class QuizTakerAnswerPanel extends Component {
  static propTypes = {
    answer:                   PropTypes.string,
    questionTotal:            PropTypes.number.isRequired,
    quizFinalized:            PropTypes.bool.isRequired,
    onAnswerChanged:          PropTypes.func.isRequired,
    onAnswerSubmitted:        PropTypes.func.isRequired,
    onQuestionSkipped:        PropTypes.func.isRequired,
    onQuestionReported:       PropTypes.func.isRequired,
    questionsAttempted:       PropTypes.number.isRequired,
    questionIsMutipleChoice:  PropTypes.bool.isRequired,
  }

  isAnswerBlank(questionIsMutipleChoice, answer) {
    return (questionIsMutipleChoice && !answer)
        || (!questionIsMutipleChoice && !answer)
        || (!questionIsMutipleChoice && answer === FREE_RESPONSE_BLANK_ANSWER)
  }

  renderAnswerer(questionIsMutipleChoice, answer, onAnswerChanged) {
    return (
      <div className={style.answerer}>
        {
          questionIsMutipleChoice
              ? <MultipleChoiceAnswerer
                  answer={answer}
                  onAnswerChanged={onAnswerChanged} />
              : <FreeResponseAnswerer
                  answer={answer}
                  onAnswerChanged={onAnswerChanged} />
        }
      </div>
    )
  }

  renderAnswererButtons(
    questionIsMutipleChoice,
    answer,
    onAnswerSubmitted,
    onQuestionSkipped,
    onQuestionReported,
    quizFinalized,
  ) {
    const submitButtonDisabled = quizFinalized
        || this.isAnswerBlank(questionIsMutipleChoice, answer)
    const submitExtraProps = submitButtonDisabled
        ? {
            'data-balloon':
                !quizFinalized
                    ? 'Must provide a valid answer'
                    : 'Change an answer to re-activate this button',
            'data-balloon-pos': 'left',
          }
        : null

    return (
      <div className={style.answererButtons}>
        <div className={style.primaryAnswererButton} {...submitExtraProps}>
          <RaisedButton
            label="Submit Answer"
            primary={true}
            onClick={onAnswerSubmitted}
            disabled={submitButtonDisabled}
            fullWidth={true} />
        </div>
        <div className={style.secondaryAnswererButtons}>
          <div
            className={style.secondaryAnswererButton}
            data-balloon="Click to skip this question"
            data-balloon-pos="left">
            <RaisedButton
              icon={
                <FontIcon color="#fff" className="material-icons">
                  skip_next
                </FontIcon>
              }
              onClick={onQuestionSkipped}
              fullWidth={true}
              backgroundColor="#444" />
          </div>
          <div
            className={style.secondaryAnswererButton}
            data-balloon="Click to report invalid question"
            data-balloon-pos="left">
            <RaisedButton
              icon={
                <FontIcon color="#fff" className="material-icons">
                  report
                </FontIcon>
              }
              onClick={onQuestionReported}
              fullWidth={true}
              backgroundColor="#c62828" />
          </div>
        </div>
      </div>
    )
  }

  renderFinishButton(questionsAttempted, questionTotal) {
    const finishButtonClassName = classNames(style.finishButton, {
      [style.finishButton__disabled]: questionsAttempted < questionTotal,
    })
    const finishButtonExtraProps = questionsAttempted < questionTotal
        ? {
            'data-balloon': 'All questions must be attempted',
            'data-balloon-pos': 'left',
          }
        : null

    return (
      <div
        className={style.finishButtonWrapper}>
        <div className={finishButtonClassName} {...finishButtonExtraProps}>
          <div className={style.finishButtonLabel}>Finish Quiz</div>
          <div className={style.finishButtonSublabel}>
            {questionsAttempted} / {questionTotal} attempted
          </div>
          <div className={style.finishButtonOverlay} />
        </div>
      </div>
    )
  }

  render() {
    const {
      answer,
      quizFinalized,
      questionTotal,
      onAnswerChanged,
      onAnswerSubmitted,
      onQuestionSkipped,
      questionsAttempted,
      onQuestionReported,
      questionIsMutipleChoice,
    } = this.props

    return (
      <div className={style.main}>
        {this.renderAnswerer(questionIsMutipleChoice, answer, onAnswerChanged)}
        {
          this.renderAnswererButtons(
            questionIsMutipleChoice,
            answer,
            onAnswerSubmitted,
            onQuestionSkipped,
            onQuestionReported,
            quizFinalized)
        }
        {this.renderFinishButton(questionsAttempted, questionTotal)}
      </div>
    )
  }
}

export default QuizTakerAnswerPanel
