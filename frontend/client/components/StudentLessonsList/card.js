
import FlatButton from 'material-ui/FlatButton'
import React from 'react'

import style from './card.css'

import { formatDateCreated, pictureNameToBackgroundURL } from 'utils'

const LESSON_STAT_ICON_CLASSNAME = `material-icons ${style.lessonStatIcon}`

const StudentLessonCard = ({
  lesson: {
    id,
    name,
    started,
    lesson,
    quiz,
    lessonFinished,
    skippedProblems,
    quizScore,
    lessonOrder,
    lessonStatus,
    lessonType,
    totalProblems,
    dateLessonFinished,
    quizSubmissionId
  }
  ,
  onLessonStartRequested,
  onLessonQuizStartRequested,
  onLessonQuizReview,
}) => (
  <div className={style.main}>
    <div className={style.info}>
      <div className={style.title}>{name}</div>
    </div>

    <div className={style.lessonStat}>
      <i className={LESSON_STAT_ICON_CLASSNAME}>info</i>
      <div className={style.lessonStatText}>
        <span>Lesson order is </span>
        <span
          className={style.lessonStatHighlight}>
          {lessonOrder}
        </span>
        <span> and has </span>
        <span
          className={style.lessonStatHighlight}>
          {totalProblems}
        </span>
        <span> problems</span>
      </div>
    </div>
    {lessonFinished === true ?
      <div className={style.lessonStat}>
        <i className={LESSON_STAT_ICON_CLASSNAME}>info</i>
        <div className={style.lessonStatText}>
          <span>Lesson finished </span>
          <span
            className={style.lessonStatHighlight}>
            {formatDateCreated(dateLessonFinished)}
          </span>
        </div>
      </div>
      :
      <span></span>
    }
    {lessonFinished === true ?
      <div className={style.lessonStat}>
        <i className={LESSON_STAT_ICON_CLASSNAME}>info</i>
        <div className={style.lessonStatText}>
          <span>Your score was </span>
          <span
            className={style.lessonStatHighlight}>
            {quizScore}
          </span>
          <span> and you skipped </span>
          <span
            className={style.lessonStatHighlight}>
            {skippedProblems}
          </span>
        </div>
      </div> :
      <div className={style.actions}>
        <div className={style.action}>
          <FlatButton
              label={'View Lesson'}
              onClick={() => onLessonStartRequested(lesson.contentItemId,name)}
              primary={true} />
        </div>
        <div className={style.action}>
          <FlatButton
              disabled={
                lessonType.code === 'Scheduled'
              }
              label="Start/Resume Quiz"
              onClick={() => onLessonQuizStartRequested(quiz.id)} />
        </div>
    </div>
    }
    {lessonFinished === true ?
      <div className={style.actions}>
        <div className={style.action}>
          <FlatButton
              label={'View Lesson'}
              onClick={() => onLessonStartRequested(lesson.contentItemId,name)}
              primary={true} />
        </div>
        <div className={style.action}>
          <FlatButton
              label={'Review Quiz'}
              onClick={() => onLessonQuizReview(quiz.id)}
              primary={true} />
        </div>
      </div>
      :
      <span></span>
    }
  </div>
)

export default StudentLessonCard
