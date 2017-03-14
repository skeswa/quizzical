
import classNames from 'classnames'
import React, { Component, PropTypes } from 'react'

import style from './clock.css'
import { hsvToRgb } from 'utils/color'

const TIME_LIMIT = 200 /* in seconds */
const CIRCUMFERENCE = 2 * Math.PI * 22 /* radius */
const BAD_TIME_COLOR = { h: 0, s: 0.797979797979798, v: 0.8764705882352941 }
const GOOD_TIME_COLOR = {
  h: 0.34177215189873417,
  s: 0.682,
  v: 0.69019607843137253,
}

class QuizTakerClock extends Component {
  static propTypes = {
    onTick:   PropTypes.func.isRequired,
    seconds:  PropTypes.number.isRequired,
  }

  intervalRef = null

  componentDidMount() {
    this.intervalRef = setInterval(() => this.props.onTick(this.props.seconds + 1), 1000)
  }

  componentWillUnmount() {
    clearInterval(this.intervalRef)
  }

  calculateClockColor(cappedSeconds) {
    const progressScalar = cappedSeconds / TIME_LIMIT

    const h1 = GOOD_TIME_COLOR.h
    const h2 = BAD_TIME_COLOR.h
    const s1 = GOOD_TIME_COLOR.s
    const s2 = BAD_TIME_COLOR.s
    const v1 = GOOD_TIME_COLOR.v
    const v2 = BAD_TIME_COLOR.v

    return hsvToRgb(
      h1 + ((h2 - h1) * progressScalar),
      s1 + ((s2 - s1) * progressScalar),
      v1 + ((v2 - v1) * progressScalar))
  }

  render() {
    const { seconds } = this.props

    const cappedSeconds = seconds > TIME_LIMIT ? TIME_LIMIT : seconds
    const dashLength = (cappedSeconds / TIME_LIMIT) * CIRCUMFERENCE
    const clockColor = this.calculateClockColor(cappedSeconds)
    const secondsFontScalar = (
      1 - ((Math.floor(Math.log10(cappedSeconds)) + 1) * 0.05))

    const backgroundRingClassName = classNames(
      style.backgroundRing,
      { [style.backgroundRing__flashing]: cappedSeconds === TIME_LIMIT })
    const foregroundRingClassName = classNames(
      style.foregroundRing,
      { [style.foregroundRing__flashing]: cappedSeconds === TIME_LIMIT })
    const secondsStyle = {
      color: clockColor,
      transform: `scale(${secondsFontScalar})`,
    }
    const foregroundRingStyle = {
      stroke: clockColor,
      strokeDasharray: `${dashLength}, ${CIRCUMFERENCE}`,
    }

    return (
      <div className={style.main}>
        <svg className={style.circle} viewBox="0 0 50 50">
          <circle
            className={backgroundRingClassName}
            cx="25"
            cy="25"
            r="22"
            fill="none"
            strokeWidth="3"
            strokeMiterlimit="10" />
          <circle
            style={foregroundRingStyle}
            className={foregroundRingClassName}
            cx="25"
            cy="25"
            r="22"
            fill="none"
            strokeWidth="3"
            strokeMiterlimit="10" />
        </svg>
        <div style={secondsStyle} className={style.seconds}>{seconds}</div>
      </div>
    )
  }
}

export default QuizTakerClock
