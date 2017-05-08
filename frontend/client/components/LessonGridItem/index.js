
import Dialog from 'material-ui/Dialog'
import autobind from 'autobind-decorator'
import React, { Component } from 'react'

import style from './style.css'
import {
  timeSince,
  textColorForBackground,
  pictureIdToBackgroundURL,
} from 'utils'

class LessonGridItem extends Component {
  state = {
    lightboxVisible: false,
  }

  @autobind
  onClick() {
    this.setState({ lightboxVisible: true })
  }

  @autobind
  onLightboxClosed() {
    this.setState({ lightboxVisible: false })
  }

  renderLightbox(lessonPictureURL) {
    const { lightboxVisible } = this.state

    return (
      <Dialog
        open={lightboxVisible}
        bodyStyle={{ padding: '0' }}
        contentStyle={{ width: '50%', minWidth: '0', maxWidth: 'none', maxHeight: '80%' }}
        overlayStyle={{ paddingTop: '0' }}
        onRequestClose={this.onLightboxClosed}>
        <img
          src={lessonPictureURL}
          className={style.lightboxPicture} />
      </Dialog>
    )
  }

  render() {
    const {
      onClick,
      lesson: {
        id: lessonId,
        lessonStatus: { name: lessonStatusName },
        lessonType: { name: lessonTypeName },
        dateCreated: lessonDateCreated,
        lessonFinished: isLessonFinished,
        quiz: { id: lessonQuizId },
      }
    } = this.props

    const lessonType            = isLessonFinished
      ? 'finished'
      : 'scheduled'
    const difficultyBgColor       = '#89f442'
    const lessonPictureURL      = pictureIdToBackgroundURL(lessonPictureId)
    const difficultyTextColor     = textColorForBackground(difficultyBgColor)
    const formattedDateCreated    = timeSince(lessonDateCreated)
    const difficultyColorStyle    = {
      color:            difficultyTextColor,
      backgroundColor:  difficultyBgColor,
    }

    return (
      <div className={style.wrapper}>
        <div className={style.main} onClick={this.onClick}>
          <div
            style={{ backgroundImage: `url(${lessonPictureURL})`}}
            className={style.picture} />
          <div
            style={difficultyColorStyle}
            className={style.difficultyColorBar} />
          <div className={style.info}>
            <div className={style.category}>{lessonCategoryName}</div>
            <div />
            <div
              className={style.difficulty}
              style={difficultyColorStyle}>{lessonDifficultyName}</div>
            <div className={style.lessonType}>{lessonType}</div>
            <div className={style.dateCreated}>Created {formattedDateCreated} ago.</div>
          </div>
        </div>

        {this.renderLightbox(lessonPictureURL)}
      </div>
    )
  }
}

export default LessonGridItem
