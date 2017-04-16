
import autobind from 'autobind-decorator'
import React, { Component } from 'react'

import style from './style.css'

class QuizAttemptGridItem extends Component {
  state = {}

  @autobind
  onClick() {
    window.alert('CLIKY TIME')
  }

  render() {
    return (
      <div className={style.wrapper}>
        <div className={style.main} onClick={this.onClick}>
          HELLO PLS, THIS IS ATTEMPT THING
        </div>
      </div>
    )
  }
}

export default QuizAttemptGridItem
