
import React, { Component } from 'react'

import style from './style.css'
import QuizAttemptGridItem from 'components/QuizAttemptGridItem'

const QuizAttemptGrid = (props) => (
  <div className={style.main}>
    <div className={style.gridItems}>
      {props.quizAttempts.map(quizAttempt => (
        <QuizAttemptGridItem
          key={quizAttempt.id}
          quizAttempt={quizAttempt} />
      ))}
    </div>
  </div>
)

export default QuizAttemptGrid
