
import FontIcon from 'material-ui/FontIcon'
import classNames from 'classnames'
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component } from 'react'

import style from './style.css'

const SKIP_BUTTON_STYLE = { width: '3.6rem', minWidth: '1.5rem' }
const BASE_QUESTION_PICTURE_URL = '/api/problems/pictures'

class QuizTaker extends Component {
  static propTypes = {
    quiz:                   React.PropTypes.object.isRequired,
    questionIndex:          React.PropTypes.number.isRequired,
    onQuestionIndexChanged: React.PropTypes.func.isRequired,
  }

  state = {
    lightboxMounted: false,
    lightboxVisible: false,
  }
  mounted   = false
  animating = false

  componentDidMount() {
    this.mounted = true
  }

  componentWillUnmount() {
    this.mounted = false
  }

  toPictureURL(pictureId) {
    return `url(${BASE_QUESTION_PICTURE_URL}/${pictureId}.png)`
  }

  onAnswerClicked() {

  }

  onQuestionClicked() {
    if (this.animating) return;

    this.animating = true
    this.setState({ lightboxMounted: true }, () => setTimeout(() => {
      if (this.mounted) {
        this.animating = false
        this.setState({ lightboxVisible: true })
      }
    }, 300))
  }

  onLightboxClicked() {
    if (this.animating) return;

    this.animating = true
    this.setState({ lightboxVisible: false }, () => setTimeout(() => {
      if (this.mounted) {
        this.animating = false
        this.setState({ lightboxMounted: false })
      }
    }, 300))
  }

  renderLightbox(questionPictureURL) {
    const { lightboxMounted, lightboxVisible } = this.state
    if (!lightboxMounted) {
      return null
    }

    return (
      <div
        onClick={::this.onLightboxClicked}
        className={classNames(style.lightbox, {
          [style.lightbox__visible]: lightboxVisible
        })}>
        <div
          style={{ backgroundImage: questionPictureURL }}
          className={style.questionPicture} />
      </div>
    )
  }

  render() {
    const { questionIndex, quiz: { questions } } = this.props
    const {
      problem: {
        multipleChoice:         questionIsMutipleChoice,
        questionPicture:        { id: questionPictureId },
        requiresCalculator:     questionRequiresCalculator,
        sourceIndexWithinPage:  questionNumber,
      },
    } = questions[questionIndex]
    const questionPictureURL = this.toPictureURL(questionPictureId)
    const questionPictureStyle = { backgroundImage: questionPictureURL }
    const questionPrompt = `Please answer question #${questionNumber}`
    const skipIcon = <FontIcon className="material-icons">skip_next</FontIcon>

    return (
      <div className={style.main}>
        <div
          onClick={::this.onQuestionClicked}
          className={style.questionPictureWrapper}>
          <div className={style.questionPrompt}>
            <span>{questionPrompt}</span>
            {
              questionRequiresCalculator
              ? <span className={style.withCalculator}>
                  with your calculator
                </span>
              : null
            }
          </div>
          <div style={questionPictureStyle} className={style.questionPicture} />
        </div>
        <div className={style.buttons}>
          <div className={style.bigButton}>
            <RaisedButton
              label="Answer"
              labelColor="#754aec"
              fullWidth={true}
              onTouchTap={::this.onAnswerClicked}
              backgroundColor="#ffffff" />
          </div>
          <div className={style.smallButton}>
            <RaisedButton
              icon={skipIcon}
              style={SKIP_BUTTON_STYLE}
              labelColor="#ffffff"
              backgroundColor="#222222" />
          </div>
        </div>

        {this.renderLightbox(questionPictureURL)}
      </div>
    )
  }
}

export default QuizTaker
