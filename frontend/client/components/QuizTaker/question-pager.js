
import FontIcon from 'material-ui/FontIcon';
import classNames from 'classnames'
import IconButton from 'material-ui/IconButton';
import React, { Component, PropTypes } from 'react'

import style from './question-pager.css'
import QuizTakerClock from './clock'

const ANSWER_STATUS_ICON_STYLE = { color: '#fff', fontSize: '1.6rem' }

class QuizTakerQuestionPager extends Component {
  static propTypes = {
    questions:            PropTypes.array.isRequired,
    responses:            PropTypes.object.isRequired,
    currentQuestionIndex: PropTypes.number.isRequired,
  }

  render() {
    // const { clockSeconds } = this.state
    const { questions, responses, currentQuestionIndex } = this.props

    const questionBoxes = questions.map((question, i) =>
        <div
          key={i}
          className={classNames(style.questionBox, {
            [style.questionBox__unvisited]:
                i !== currentQuestionIndex &&
                (!responses[i] || !responses[i].answer || responses[i].skipped),
            [style.questionBox__selected]: i === currentQuestionIndex,
          })}>
          <div className={style.questionNumber}>{i + 1}</div>
          <div className={style.answerStatus}>
            <FontIcon
              style={ANSWER_STATUS_ICON_STYLE}
              className="material-icons">
              {
                responses[i] && responses[i].answer
                    ? 'chat_bubble'
                    : responses[i] && responses[i].skipped
                        ? 'skip_next'
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
