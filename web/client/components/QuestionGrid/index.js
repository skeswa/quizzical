
import React, { Component } from 'react'

import style from './style.css'
import QuestionGridItem from 'components/QuestionGridItem'

const QuestionGrid = (props) => (
  <div className={style.main}>
    <div className={style.gridItems}>
      {props.questions.map(question => (
        <QuestionGridItem
          key={question.id}
          question={question} />
      ))}
    </div>
  </div>
)

export default QuestionGrid
