
import FlatButton from 'material-ui/FlatButton'
import React from 'react'

import style from './card.css'

import { formatDateCreated, pictureNameToBackgroundURL } from 'utils'

const QUIZ_STAT_ICON_CLASSNAME = `material-icons ${style.quizStatIcon}`

const StudentQuizSubmissionCard = ({
  submission: {
    id,
    code,
    dateCreated,
    totalQuestions,
    quiz,
    quizScore,
    skipped
  }
  ,
  onQuizSubmissionReview,
}) => (
  <div className={style.main}>
    <div className={style.info}>
      <div className={style.title}>Quiz #{quiz.id}</div>
    </div>
    <div className={style.quizStat}>
      <i className={QUIZ_STAT_ICON_CLASSNAME}>info</i>
      <div className={style.quizStatText}>
        <span> Your score is </span>
        <span
          className={style.quizStatHighlight}>
          {quizScore}%
        </span>
      </div>
    </div>
    <div className={style.quizStat}>
      <i className={QUIZ_STAT_ICON_CLASSNAME}>info</i>
      <div className={style.quizStatText}>
        <span> You skipped </span>
        <span
          className={style.quizStatHighlight}>
          {skipped}
        </span>
        <span> problems</span>
      </div>
    </div>
    <div className={style.quizStat}>
      <i className={QUIZ_STAT_ICON_CLASSNAME}>info</i>
      <div className={style.quizStatText}>
        <span> Quiz has </span>
        <span
          className={style.quizStatHighlight}>
          {totalQuestions}
        </span>
        <span> problems</span>
      </div>
    </div>
    <div className={style.quizStat}>
      <i className={QUIZ_STAT_ICON_CLASSNAME}>info</i>
      <div className={style.quizStatText}>
        <span>Finished </span>
        <span
          className={style.quizStatHighlight}>
          {formatDateCreated(dateCreated)}
        </span>
      </div>
    </div>
      <div className={style.actions}>
        <div className={style.action}>
          <FlatButton
              label={'View Results'}
              onClick={() => onQuizSubmissionReview(quiz.id,code)}
              primary={true} />
        </div>
    </div>
  </div>
)

export default StudentQuizSubmissionCard
