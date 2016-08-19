
import React, { Component } from 'react'

import style from './style.css'
import {
  timeSince,
  textColorForBackground,
  pictureNameToBackgroundUrl,
} from './helpers'

class QuestionListItem extends Component {
  state = {
    questionPictureLoaded: false,
  }

  onQuestionPictureLoaded() {
    this.setState({ questionPictureLoaded: true })
  }

  render() {
    const {
      onClick,
      question: {
        id: questionId,
        category: { name: questionCategoryName },
        difficulty: {
          name: questionDifficultyName,
          color: questionDifficultyColor,
        },
        dateCreated: questionDateCreated,
        multipleChoice: isQuestionMultipleChoice,
        questionPicture: questionPicture,
      }
    } = this.props

    const questionType            = isQuestionMultipleChoice
      ? 'multiple choice'
      : 'numeric answer'
    const difficultyBgColor       = `#${questionDifficultyColor}`
    const questionPictureURL      = pictureNameToBackgroundUrl(questionPicture)
    const difficultyTextColor     = textColorForBackground(difficultyBgColor)
    const formattedDateCreated    = timeSince(questionDateCreated)
    const difficultyColorStyle    = {
      color:            difficultyTextColor,
      backgroundColor:  difficultyBgColor,
    }

    return (
      <div className={style.listItem} onClick={onClick}>
        <img
          src={questionPictureURL}
          onLoad={::this.onQuestionPictureLoaded}
          className={style.listItemPicture} />
        <div
          style={difficultyColorStyle}
          className={style.listItemDifficultyColorBar} />
        <div className={style.listItemInfo}>
          <div className={style.listItemCategory}>{questionCategoryName}</div>
          <div />
          <div
            className={style.listItemDifficulty}
            style={difficultyColorStyle}>{questionDifficultyName}</div>
          <div className={style.listItemQuestionType}>{questionType}</div>
          <div className={style.listItemDateCreated}>Created {formattedDateCreated} ago.</div>
        </div>
      </div>
    )
  }
}

export default QuestionListItem
