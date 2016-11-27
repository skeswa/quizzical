
import Dialog from 'material-ui/Dialog'
import React, { Component } from 'react'

import style from './style.css'
import {
  timeSince,
  textColorForBackground,
  pictureIdToBackgroundURL,
} from 'utils'

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
        questionPicture: { id: questionPictureId },
      }
    } = this.props

    const questionType            = isQuestionMultipleChoice
      ? 'multiple choice'
      : 'numeric answer'
    const difficultyBgColor       = questionDifficultyColor
    const questionPictureURL      = pictureIdToBackgroundURL(questionPictureId)
    const difficultyTextColor     = textColorForBackground(difficultyBgColor)
    const formattedDateCreated    = timeSince(questionDateCreated)
    const difficultyColorStyle    = {
      color:            difficultyTextColor,
      backgroundColor:  difficultyBgColor,
    }

    return (
      <div className={style.wrapper}>
        <div className={style.main} onClick={::this.onClick} >
          <div
            style={{ backgroundImage: `url(${questionPictureURL})`}}
            className={style.picture} />
          <div
            style={difficultyColorStyle}
            className={style.difficultyColorBar} />
          <div className={style.info}>
            <div className={style.category}>{questionCategoryName}</div>
            <div />
            <div
              className={style.difficulty}
              style={difficultyColorStyle}>{questionDifficultyName}</div>
            <div className={style.questionType}>{questionType}</div>
            <div className={style.dateCreated}>Created {formattedDateCreated} ago.</div>
          </div>
        </div>

        {this.renderLightbox(questionPictureURL)}
      </div>
    )
  }
}

export default QuestionGridItem
