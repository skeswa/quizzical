
import FontIcon from 'material-ui/FontIcon'
import classNames from 'classnames'
import IconButton from 'material-ui/IconButton'
import React, { Component, PropTypes } from 'react'

import style from './question-pager.css'
import QuizTakerClock from './clock'

const ANSWER_STATUS_ICON_STYLE = { color: 'inherit', fontSize: '1.6rem' }

class QuizTakerQuestionPager extends Component {
  static propTypes = {
    questions:                      PropTypes.array.isRequired,
    responses:                      PropTypes.object.isRequired,
    currentQuestionIndex:           PropTypes.number.isRequired,
    onCurrentQuestionIndexChanged:  PropTypes.func.isRequired,
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

  render() {
    const {
      questions,
      responses,
      currentQuestionIndex,
      onCurrentQuestionIndexChanged,
    } = this.props

    const questionBoxes = questions.map((question, i) =>
        <div
          key={i}
          onClick={onCurrentQuestionIndexChanged}
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
              style={ANSWER_STATUS_ICON_STYLE}
              className="material-icons">
              {
                this.isAnswered(i, responses)
                    ? 'chat_bubble'
                    : 'chat_bubble_outline'
              }
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
