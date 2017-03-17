
import FontIcon from 'material-ui/FontIcon'
import classNames from 'classnames'
import IconButton from 'material-ui/IconButton'
import React, { Component, PropTypes } from 'react'

import style from './header.css'
import QuizTakerClock from './clock'

const BACK_ARROW_ICON_STYLE = { color: '#fff', fontSize: '2rem' }

class QuizTakerHeader extends Component {
  static propTypes = {
    questionIndex:        PropTypes.number.isRequired,
    questionTotal:        PropTypes.number.isRequired,
    questionNumberInPage: PropTypes.any.isRequired,
  }

  state = { clockSeconds: 0 }

  onClockTick(clockSeconds) {
    this.setState({ clockSeconds })
  }

  onDeleteClicked() {
    // TODO(skeswa): implement this right here.
  }

  render() {
    const { clockSeconds } = this.state
    const { questionIndex, questionTotal, questionNumberInPage } = this.props

    return (
      <div className={style.main}>
        <div className={style.deleteButton}>
          <IconButton
            onClick={::this.onDeleteClicked}
            iconStyle={BACK_ARROW_ICON_STYLE}
            focusRippleColor="#ffffff"
            touchRippleColor="#ffffff">
            <FontIcon className="material-icons">menu</FontIcon>
          </IconButton>
        </div>
        <div className={style.info}>
          <div className={style.currentQuestion}>
            Question {questionIndex + 1} of {questionTotal}
          </div>
          <div className={style.questionInstructions}>
            Please respond to prompt <span className={style.highlight}>#{questionNumberInPage}</span> below
          </div>
        </div>
        <div className={style.timer}>
          <QuizTakerClock
            onTick={::this.onClockTick}
            seconds={clockSeconds} />
        </div>
      </div>
    )
  }
}

export default QuizTakerHeader
