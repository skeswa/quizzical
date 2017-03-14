
import classNames from 'classnames'
import RefreshIndicator from 'material-ui/RefreshIndicator'
import React, { Component, PropTypes } from 'react'

import style from './style.css'
import Lightbox from './lightbox'

const CALCULATOR_PROMPT = 'with your calculator'
const BASE_QUESTION_PICTURE_URL = '/api/problems/pictures'

class QuestionPicture extends Component {
  static propTypes = {
    style:              PropTypes.object,
    questionId:         PropTypes.number,
    pictureId:          PropTypes.number.isRequired,
    questionNumber:     PropTypes.number,
    onPictureLoaded:    PropTypes.func,
    requiresCalculator: PropTypes.bool,
  }

  state = {
    pictureLoaded:    false,
    lightboxVisible:  false,
  }

  getPictureURL() {
    const { pictureId } = this.props

    return `${BASE_QUESTION_PICTURE_URL}/${pictureId}.png`
  }

  onPictureLoaded() {
    const { onPictureLoaded } = this.props

    this.setState({ pictureLoaded: true })
    if (onPictureLoaded) onPictureLoaded()
  }

  onPictureClicked() {
    this.setState({ lightboxVisible: true })
  }

  onLightboxDismissal() {
    this.setState({ lightboxVisible: false })
  }

  renderPrompt() {
    const { questionNumber, questionId, requiresCalculator } = this.props

    // Don't render a prompt if there is not question number.
    if (!questionNumber && questionNumber !== 0) {
      return null
    }

    return (
      <div className={style.questionPrompt}>
        <span>{`Please answer question #${questionNumber}/${questionId}`}</span>
        {
          requiresCalculator
            ? <span className={style.withCalculator}>
              </span>
            : null
        }
      </div>
    )
  }

  render() {
    const pictureURL = this.getPictureURL()
    const { pictureLoaded, lightboxVisible } = this.state
    const { style: _style, prompt, pictureId, questionId } = this.props

    const pictureStyle = { backgroundImage: `url(${pictureURL})` }
    const pictureClassName = classNames(style.picture, {
      [style.picture__loaded]: pictureLoaded,
    })
    const pictureLoaderStyle = { opacity: pictureLoaded ? 0 : 1 }

    return (
      <div style={_style} className={style.main}>
        {this.renderPrompt()}

        <img
          src={pictureURL}
          onLoad={::this.onPictureLoaded}
          className={style.pictureLoadingHelper} />
        <div className={style.pictureLoaderWrapper}>
          <div style={pictureLoaderStyle} className={style.pictureLoader}>
            <RefreshIndicator
              top={0}
              left={0}
              size={50}
              status="loading"
              loadingColor="#754aec" />
          </div>
        </div>
        <div
          style={pictureStyle}
          onClick={::this.onPictureClicked}
          className={pictureClassName} />
        <Lightbox
          visible={lightboxVisible}
          pictureURL={pictureURL}
          onDismissal={::this.onLightboxDismissal} />
      </div>
    )
  }
}

export default QuestionPicture
