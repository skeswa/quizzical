
import FontIcon from 'material-ui/FontIcon'
import classNames from 'classnames'
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component, PropTypes } from 'react'

import style from './answer-panel.css'
import FreeResponseAnswerer from 'components/FreeResponseAnswerer'
import MultipleChoiceAnswerer from 'components/MultipleChoiceAnswerer'

class QuizTakerAnswerPanel extends Component {
  static propTypes = {
  }

  state = {
    answer: null,
  }

  onAnswerChanged(answer) {
    this.setState({ answer })
  }

  onSubmitAnswerClicked() {
    // TODO
  }

  onSkipQuestionClicked() {
    // TODO
  }

  onReportQuestionClicked() {
    // TODO
  }

  renderAnswerer(questionIsMutipleChoice, answer, skipped) {
    return (
      <div className={style.answerer}>
        {
          questionIsMutipleChoice
              ? <MultipleChoiceAnswerer
                  answer={answer}
                  skipped={skipped}
                  onAnswerChanged={::this.onAnswerChanged} />
              : <FreeResponseAnswerer
                  answer={answer}
                  skipped={skipped}
                  onAnswerChanged={::this.onAnswerChanged} />
        }
      </div>
    )
  }

  renderAnswererButtons(answer) {
    return (
      <div className={style.answererButtons}>
        <div className={style.primaryAnswererButton}>
          <RaisedButton
            label="Submit Answer"
            primary={true}
            onClick={::this.onSubmitAnswerClicked}
            disabled={!answer}
            fullWidth={true} />
        </div>
        <div className={style.secondaryAnswererButtons}>
          <div className={style.secondaryAnswererButton}>
            <RaisedButton
              icon={
                <FontIcon color="#fff" className="material-icons">
                  skip_next
                </FontIcon>
              }
              onClick={::this.onSkipQuestionClicked}
              fullWidth={true}
              backgroundColor="#444" />
          </div>
          <div className={style.secondaryAnswererButton}>
            <RaisedButton
              icon={
                <FontIcon color="#fff" className="material-icons">
                  report
                </FontIcon>
              }
              onClick={::this.onReportQuestionClicked}
              fullWidth={true}
              backgroundColor="#c62828" />
          </div>
        </div>
      </div>
    )
  }

  render() {
    const { answer } = this.state

    return (
      <div className={style.main}>
        {this.renderAnswerer(false)}
        {this.renderAnswererButtons(answer)}

        <div>
          <RaisedButton
            label="Finish Quiz"
            onClick={::this.onSubmitAnswerClicked}
            disabled={!answer}
            fullWidth={true} />
        </div>
      </div>
    )
  }
}

export default QuizTakerAnswerPanel
