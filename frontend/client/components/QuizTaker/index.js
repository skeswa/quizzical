
import React, { Component } from 'react'
import style from './style.css'

class QuizTaker extends Component {
  render() {
    console.log('le props', this.props)
    
    return (
      <div className={style.main}>
        quiz taker
      </div>
    )
  }
}

export default QuizTaker
