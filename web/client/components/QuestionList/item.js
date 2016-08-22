
import Dialog from 'material-ui/Dialog'
import React, { Component } from 'react'

import style from './style.css'
import {
  timeSince,
  textColorForBackground,
  pictureNameToBackgroundUrl,
} from './helpers'

class QuestionGridItem extends Component {
  state = {
    lightboxVisible: false,
  }

  onClick() {
    this.setState({ lightboxVisible: true })
  }

  onLightboxClosed() {
    this.setState({ lightboxVisible: false })
  }

  renderLightbox(questionPictureURL) {
    const { lightboxVisible } = this.state

    return (
      <Dialog
        open={lightboxVisible}
        bodyStyle={{ padding: '0' }}
        contentStyle={{ width: '50%', minWidth: '0', maxWidth: 'none', maxHeight: '80%' }}
        overlayStyle={{ paddingTop: '0' }}
        onRequestClose={::this.onLightboxClosed}>
        <img
          src={questionPictureURL}
          className={style.lightboxPicture} />
      </Dialog>
    )
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
      <div className={style.gridItemWrapper}>
        <div className={style.gridItem} onClick={::this.onClick} >
          <div
            style={{ backgroundImage: `url(${questionPictureURL})`}}
            className={style.gridItemPicture} />
          <div
            style={difficultyColorStyle}
            className={style.gridItemDifficultyColorBar} />
          <div className={style.gridItemInfo}>
            <div className={style.gridItemCategory}>{questionCategoryName}</div>
            <div />
            <div
              className={style.gridItemDifficulty}
              style={difficultyColorStyle}>{questionDifficultyName}</div>
            <div className={style.gridItemQuestionType}>{questionType}</div>
            <div className={style.gridItemDateCreated}>Created {formattedDateCreated} ago.</div>
          </div>
        </div>

        {this.renderLightbox(questionPictureURL)}
      </div>
    )
  }
}

export default QuestionGridItem
