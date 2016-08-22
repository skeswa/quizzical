
import React, { Component } from 'react'

import style from './style.css'
import QuestionGridItem from './item'

const QuestionGrid = (props) => (
  <div className={style.list}>
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
