
import FlatButton from 'material-ui/FlatButton'
import React from 'react'

import style from './card.css'

const StudentLessonCard = ({
  lesson: { id, chapter, name, section, started, lesson, quiz },
  onLessonStartRequested,
  onLessonQuizStartRequested,
}) => (
  <div className={style.main}>
    <div className={style.info}>
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
            onClick={() => onLessonStartRequested(lesson.contentItemId,name)}
            primary={true} />
      </div>
      <div className={style.action}>
        <FlatButton
            label="Skip to quiz"
            onClick={() => onLessonQuizStartRequested(quiz.id)} />
      </div>
    </div>
  </div>
)

export default StudentLessonCard
