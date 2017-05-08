
import React, { Component } from 'react'

import style from './style.css'
import LessonGridItem from 'components/LessonGridItem'

const LessonGrid = (props) => (
  <div className={style.main}>
    <div className={style.gridItems}>
      {props.lessons.map(lesson => (
        <LessonGridItem
          key={lesson.id}
          lesson={lesson} />
      ))}
    </div>
  </div>
)

export default LessonGrid
