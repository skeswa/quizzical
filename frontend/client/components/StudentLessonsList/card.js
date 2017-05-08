
import FlatButton from 'material-ui/FlatButton'
import React from 'react'

import style from './card.css'

const StudentLessonCard = ({
  lesson: { id, chapter, name, section, started },
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
            label={
              started
                  ? 'Resume Lesson'
                  : 'Start Lesson'
            }
            onClick={() => onLessonStartRequested(id)}
            primary={true} />
      </div>
      <div className={style.action}>
        <FlatButton
            label="Skip to quiz"
            onClick={() => onLessonQuizStartRequested(id)} />
      </div>
    </div>
  </div>
)

export default StudentLessonCard