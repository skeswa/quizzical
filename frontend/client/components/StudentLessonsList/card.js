
import FlatButton from 'material-ui/FlatButton'
import React from 'react'

import style from './index.css'

const StudentLessonCard = ({
  lesson: { id, chapter, name, section },
  onLessonStartRequested,
  onLessonQuizStartRequested,
}) => (
  <div className={style.main}>
    <div className={style.info}>
      <div className={style.label}>Lesson {chapter}-{section}</div>
      <div className={style.title}>{name}</div>
    </div>
    <div className={style.actions}>
      <div className={style.action}>
        <FlatButton
            label="Skip to quiz"
            onClick={() => onLessonQuizStartRequested(id)} />
      </div>
      <div className={style.action}>
        <FlatButton
            label="Start Lesson"
            primary={true}
            onClick={() => onLessonStartRequested(id)} />
      </div>
    </div>
  </div>
)

export default StudentLessonCard