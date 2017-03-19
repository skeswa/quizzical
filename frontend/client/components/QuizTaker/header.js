
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
    questionTimeLimit:    PropTypes.number.isRequired,
    timeQuestionStarted:  PropTypes.number,
    questionNumberInPage: PropTypes.any.isRequired,
  }

  state = { clockSeconds: 0 }
  mounted = false
  intervalId = null

  componentDidMount() {
    this.mounted = true
    this.intervalId = setInterval(::this.onClockTick, 1000)
  }

  componentWillReceiveProps(nextProps) {
    const nextTimeQuestionStarted = nextProps.timeQuestionStarted
    const currentTimeQuestionStarted = this.props.timeQuestionStarted

    if (nextTimeQuestionStarted !== currentTimeQuestionStarted) {
      this.updateClockSeconds(nextTimeQuestionStarted)
    }
  }

  componentWillUnmount() {
    this.mounted = false
    clearInterval(this.intervalId)
  }

  onClockTick() {
    if (this.mounted) {
      this.updateClockSeconds(this.props.timeQuestionStarted)
    }
  }

  onMenuClicked() {
    // TODO(skeswa): the sidebar menu.
  }

  updateClockSeconds(timeQuestionStarted) {
    if (!timeQuestionStarted) {
      this.setState({ clockSeconds: 0 })
      return
    }

    this.setState({
      clockSeconds: Math.round((Date.now() - timeQuestionStarted) / 1000),
    })
  }

  render() {
    const { clockSeconds } = this.state
    const {
      questionIndex,
      questionTotal,
      questionTimeLimit,
      questionNumberInPage,
    } = this.props

    return (
      <div className={style.main}>
        <div className={style.menuButton}>
          <IconButton
            onClick={::this.onMenuClicked}
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
            <span>Please respond to prompt </span>
            <span className={style.highlight}>#{questionNumberInPage}</span>
            <span> below</span>
          </div>
        </div>
        <div className={style.timer}>
          <QuizTakerClock
            onTick={::this.onClockTick}
            seconds={clockSeconds}
            timeLimit={questionTimeLimit} />
        </div>
      </div>
    )
  }
}

export default QuizTakerHeader
