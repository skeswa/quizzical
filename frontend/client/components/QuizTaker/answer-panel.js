
import FreeResponseAnswerer from 'components/FreeResponseAnswerer'
import MultipleChoiceAnswerer from 'components/MultipleChoiceAnswerer'
import React, { Component, PropTypes } from 'react'

import style from './answer-panel.css'

class QuizTakerAnswerPanel extends Component {
  static propTypes = {
  }

  onAnswerChanged() {
    // TODO
  }

  renderAnswerer(questionIsMutipleChoice) {
    return (
      questionIsMutipleChoice
          ? <MultipleChoiceAnswerer
              onAnswerChanged={::this.onAnswerChanged} />
          : <FreeResponseAnswerer
              onAnswerChanged={::this.onAnswerChanged} />
    )
  }

  render() {
    return (
      <div className={style.main}>
        {this.renderAnswerer(false)}
      </div>
    )
  }
}

export default QuizTakerAnswerPanel
