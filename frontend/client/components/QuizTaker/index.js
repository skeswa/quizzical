
import Dialog from 'material-ui/Dialog'
import FontIcon from 'material-ui/FontIcon'
import classNames from 'classnames'
import FlatButton from 'material-ui/FlatButton'
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component } from 'react'

import style from './style.css'
import FreeResponseAnswerer from 'components/FreeResponseAnswerer'
import MultipleChoiceAnswerer from 'components/MultipleChoiceAnswerer'

const DIALOG_BODY_STYLE = { padding: '0' }
const SKIP_BUTTON_STYLE = { width: '3.6rem', minWidth: '1.5rem' }
const BASE_QUESTION_PICTURE_URL = '/api/problems/pictures'

class QuizTaker extends Component {
  static propTypes = {
    quiz:                   React.PropTypes.object.isRequired,
    questionIndex:          React.PropTypes.number.isRequired,
    onQuizFinished:         React.PropTypes.func.isRequired,
    onQuestionIndexChanged: React.PropTypes.func.isRequired,
  }

  state = {
    responses:            {},
    currentAnswer:        null,
    lightboxMounted:      false,
    lightboxVisible:      false,
    answerPopupVisible:   false,
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

  onSkipClicked() {
    const {
      quiz: { questions: { length: questionTotal } },

      questionIndex,
      onQuizFinished,
      onQuestionIndexChanged,
    } = this.props

    questionIndex >= (questionTotal - 1)
      ? onQuizFinished(this.state.responses)
      : onQuestionIndexChanged(questionIndex + 1)
  }

  onAnswerChanged(answer) {
    this.setState({ currentAnswer: answer })
  }

  onAnswerClicked() {
    this.setState({ answerPopupVisible: true })
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

  onAnswerPopupSubmitted() {
    const { responses, currentAnswer } = this.state
    const {
      quiz: { questions: { length: questionTotal } },

      questionIndex,
      onQuizFinished,
      onQuestionIndexChanged,
    } = this.props

    this.setState({
      responses: Object.assign({}, responses, {
        [questionIndex]: { answer: currentAnswer },
      }),
      currentAnswer: null,
      answerPopupVisible: false,
    }, () =>
      questionIndex >= (questionTotal - 1)
        ? onQuizFinished()
        : onQuestionIndexChanged(questionIndex + 1))
  }

  onAnswerPopupDismissed() {
    this.setState({ currentAnswer: null, answerPopupVisible: false })
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

  renderAnswerPopup(questionIsMutipleChoice) {
    const { currentAnswer, answerPopupVisible } = this.state

    const actions = [
      <FlatButton
        label="Cancel"
        onClick={::this.onAnswerPopupDismissed} />,
      <FlatButton
        label="Answer"
        primary={true}
        onClick={::this.onAnswerPopupSubmitted}
        disabled={currentAnswer === null || currentAnswer === undefined} />,
    ]

    return (
      <Dialog
        open={answerPopupVisible}
        modal={true}
        title={
          questionIsMutipleChoice
          ? 'Multiple Choice Answer'
          : 'Free Response Answer'
        }
        actions={actions}
        bodyStyle={DIALOG_BODY_STYLE}
        onRequestClose={::this.onAnswerPopupDismissed}>
        {
          questionIsMutipleChoice
          ? <MultipleChoiceAnswerer onAnswerChanged={::this.onAnswerChanged} />
          : <FreeResponseAnswerer onAnswerChanged={::this.onAnswerChanged} />
        }
      </Dialog>
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
              onClick={::this.onAnswerClicked}
              fullWidth={true}
              labelColor="#754aec"
              backgroundColor="#ffffff" />
          </div>
          <div className={style.smallButton}>
            <RaisedButton
              icon={skipIcon}
              style={SKIP_BUTTON_STYLE}
              onClick={::this.onSkipClicked}
              labelColor="#ffffff"
              backgroundColor="#222222" />
          </div>
        </div>

        {this.renderLightbox(questionPictureURL)}
        {this.renderAnswerPopup(questionIsMutipleChoice)}
      </div>
    )
  }
}

export default QuizTaker
