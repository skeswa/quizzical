
import FontIcon from 'material-ui/FontIcon'
import classNames from 'classnames'
import IconButton from 'material-ui/IconButton'
import React, { Component, PropTypes } from 'react'

import style from './question-pager.css'
import QuizTakerClock from './clock'

const ANSWER_ICON_STYLE = {
  color: 'inherit',
  width: '1.6rem',
  fontSize: '1.6rem',
}
const REPORTED_ICON_STYLE = {
  left: '-0.1rem',
  color: 'inherit',
  width: '1.6rem',
  fontSize: '1.8rem',
}
const SKIPPED_ICON_STYLE = {
  left: '-0.4rem',
  color: 'inherit',
  width: '1.6rem',
  fontSize: '2.5rem',
}

class QuizTakerQuestionPager extends Component {
  static propTypes = {
    questions:                      PropTypes.array.isRequired,
    responses:                      PropTypes.object.isRequired,
    currentQuestionIndex:           PropTypes.number.isRequired,
    onCurrentQuestionIndexChanged:  PropTypes.func.isRequired,
  }

  getIcon(index, responses) {
    const response = responses[index]

    if (!response) return 'chat_bubble_outline'
    if (response.answer !== null) return 'chat_bubble'
    if (response.reported) return 'report'
    if (response.skipped !== null) return 'skip_next'

    return 'chat_bubble_outline'
  }

  getIconStyle(index, responses) {
    const response = responses[index]

    if (!response) return ANSWER_ICON_STYLE
    if (response.answer !== null) return ANSWER_ICON_STYLE
    if (response.reported) return REPORTED_ICON_STYLE
    if (response.skipped !== null) return SKIPPED_ICON_STYLE

    return ANSWER_ICON_STYLE
  }

  isSelected(index, questionIndex) {
    return index === questionIndex
  }

  isAttempted(index, responses) {
    return !!responses[index]
  }

  isAnswered(index, responses) {
    return responses[index] && responses[index].answer !== null
  }

  onQuestionBoxClicked(questionBoxIndex) {
    const { currentQuestionIndex, onCurrentQuestionIndexChanged } = this.props

    if (questionBoxIndex !== currentQuestionIndex) {
      onCurrentQuestionIndexChanged(questionBoxIndex)
    }
  }

  render() {
    const { questions, responses, currentQuestionIndex } = this.props

    const questionBoxes = questions.map((question, i) =>
        <div
          key={i}
          onClick={this.onQuestionBoxClicked.bind(this, i)}
          className={classNames(style.questionBox, {
            [style.questionBox__unattempted]:
                !this.isSelected(i, currentQuestionIndex)
                    && !this.isAttempted(i, responses),
            [style.questionBox__selected]:
                this.isSelected(i, currentQuestionIndex),
          })}>
          <div className={style.questionNumber}>{i + 1}</div>
          <div className={style.answerStatus}>
            <FontIcon
              style={this.getIconStyle(i, responses)}
              className="material-icons">
              {this.getIcon(i, responses)}
            </FontIcon>
          </div>
        </div>)

    return (
      <div className={style.main}>
        <div className={style.questionBoxes}>
          {questionBoxes}
        </div>
      </div>
    )
  }
}

export default QuizTakerQuestionPager
