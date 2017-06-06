
import FlatButton from 'material-ui/FlatButton'
import React from 'react'

import style from './card.css'

import { formatDateCreated, pictureNameToBackgroundURL } from 'utils'

const QUIZ_STAT_ICON_CLASSNAME = `material-icons ${style.quizStatIcon}`

const StudentQuizCard = ({
  quiz: {
    id,
    code,
    created,
    questions
  }
  ,
  onQuizCancelRequested,
  onQuizStartRequested,
}) => (
  <div className={style.main}>
    <div className={style.info}>
      <div className={style.title}>{code}</div>
    </div>

    <div className={style.quizStat}>
      <i className={QUIZ_STAT_ICON_CLASSNAME}>info</i>
      <div className={style.quizStatText}>
        <span> Quiz has </span>
        <span
          className={style.quizStatHighlight}>
          {questions.length}
        </span>
        <span> questions</span>
      </div>
    </div>
    <div className={style.actions}>
      <div className={style.action}>
        <FlatButton
            label={'Restart Quiz'}
            onClick={() => onQuizStartRequested(quiz.contentItemId,name)}
            primary={true} />
      </div>
    </div>
  </div>
)

export default StudentQuizCard
