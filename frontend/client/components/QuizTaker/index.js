
import Dialog from 'material-ui/Dialog'
import FontIcon from 'material-ui/FontIcon'
import Snackbar from 'material-ui/Snackbar'
import classNames from 'classnames'
import FlatButton from 'material-ui/FlatButton'
import RaisedButton from 'material-ui/RaisedButton'
import RefreshIndicator from 'material-ui/RefreshIndicator'
import React, { Component } from 'react'

import style from './style.css'
import QuestionPicture from 'components/QuestionPicture'
import FreeResponseAnswerer from 'components/FreeResponseAnswerer'
import MultipleChoiceAnswerer from 'components/MultipleChoiceAnswerer'

const DIALOG_BODY_STYLE = { padding: '0' }
const SKIP_BUTTON_STYLE = { width: '3.6rem', minWidth: '1.5rem' }
const TOAST_AUTOHIDE_DURATION = 1200
const RESPONSE_REVERSAL_MESSAGE = 'Last question response reversed ' +
  'successfully.'

class QuizTaker extends Component {
  static propTypes = {
    quiz:                   React.PropTypes.object.isRequired,
    questionIndex:          React.PropTypes.number.isRequired,
    onQuizFinished:         React.PropTypes.func.isRequired,
    onQuizCancelled:        React.PropTypes.func.isRequired,
    onQuestionIndexChanged: React.PropTypes.func.isRequired,
  }

  state = {
    responses:                    {},
    undoRequested:                false,
    currentAnswer:                null,
    lightboxMounted:              false,
    lightboxVisible:              false,
    answerPopupVisible:           false,
    notificationToastVisible:     false,
    notificationToastMessage:     '',
    timeCurrentQuestionStarted:   null,
    notificationToastReversable:  true,
  }
  mounted   = false
  animating = false

  componentDidMount() {
    this.mounted = true
  }

  componentWillUnmount() {
    this.mounted = false
  }

  componentWillReceiveProps(nextProps) {
    const { questionIndex: nextQuestionIndex } = nextProps
    const { questionIndex: currentQuestionIndex } = this.props

    if (currentQuestionIndex !== nextQuestionIndex) {
      if (this.state.undoRequested
          && (nextQuestionIndex < currentQuestionIndex)) {
        this.setState({
          undoRequested:                false,
          notificationToastVisible:     true,
          notificationToastMessage:     RESPONSE_REVERSAL_MESSAGE,
          timeCurrentQuestionStarted:   null,
          notificationToastReversable:  false,
        })
      } else if (nextQuestionIndex > currentQuestionIndex) {
        const pastParticiple = this.state.responses
            .hasOwnProperty(currentQuestionIndex)
          ? 'answered'
          : 'skipped'

        this.setState({
          undoRequested:                false,
          notificationToastVisible:     true,
          notificationToastMessage:     `Question #${currentQuestionIndex + 1} `
              + `${pastParticiple} successfully.`,
          timeCurrentQuestionStarted:   null,
          notificationToastReversable:  true,
        })
      } else {
        this.setState({
          timeCurrentQuestionStarted: null,
        })
      }
    }
  }

  composeSubmission() {
    const { quiz: { id: quizId, questions } } = this.props
    const { responses } = this.state

    if (!questions || !responses) {
      return []
    }

    return {
      quizId,
      responses: questions.map((question, i) => {
        const {
          answer = null,
          skipped = true,
          secondsElapsed = 0,
        } = responses[i] || {}
        const { id: quizProblemId } = question

        return {
          skipped,
          quizProblemId,

          response: answer,
          secondsElapsed: Math.round(secondsElapsed),
        }
      }),
    }
  }

  requestUndo() {
    const { questionIndex, onQuestionIndexChanged } = this.props

    if (questionIndex > 0) {
      this.setState(
          { undoRequested: true },
          () => onQuestionIndexChanged(questionIndex - 1))
    }
  }

  onSkipClicked() {
    const {
      quiz: { questions: { length: questionTotal } },

      questionIndex,
      onQuizFinished,
      onQuestionIndexChanged,
    } = this.props
    const { responses, timeCurrentQuestionStarted } = this.state

    this.setState({
      responses: Object.assign({}, responses, {
        [questionIndex]: {
          answer: null,
          skipped: true,
          secondsElapsed: timeCurrentQuestionStarted !== null
            ? (Date.now() - timeCurrentQuestionStarted) / 1000
            : 0,
        },
      }),
    })

    questionIndex >= (questionTotal - 1)
      ? onQuizFinished(this.composeSubmission())
      : onQuestionIndexChanged(questionIndex + 1)
  }

  onNotificationToastActionClicked(e) {
    e.preventDefault()
    this.requestUndo()
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

  onAnswerPopupSubmitted() {
    const { responses, currentAnswer, timeCurrentQuestionStarted } = this.state
    const {
      quiz: { questions: { length: questionTotal } },

      questionIndex,
      onQuizFinished,
      onQuestionIndexChanged,
    } = this.props

    this.setState({
      responses: Object.assign({}, responses, {
        [questionIndex]: {
          answer: currentAnswer,
          skipped: false,
          secondsElapsed: timeCurrentQuestionStarted !== null
            ? (Date.now() - timeCurrentQuestionStarted) / 1000
            : 0,
        },
      }),
      currentAnswer: null,
      answerPopupVisible: false,
    }, () =>
      questionIndex >= (questionTotal - 1)
        ? onQuizFinished(this.composeSubmission())
        : onQuestionIndexChanged(questionIndex + 1))
  }

  onAnswerPopupDismissed() {
    this.setState({ currentAnswer: null, answerPopupVisible: false })
  }

  onQuestionPictureLoaded() {
    this.setState({
      timeCurrentQuestionStarted: Date.now(),
    })
  }

  onNotificationToastDismissed() {
    this.setState({ notificationToastVisible: false })
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
        disabled={!currentAnswer} />,
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

  renderNotificationToast() {
    const {
      notificationToastVisible,
      notificationToastMessage,
      notificationToastReversable,
    } = this.state

    return (
      <Snackbar
        open={notificationToastVisible}
        action={
          notificationToastReversable
            ? 'Undo'
            : null
        }
        message={notificationToastMessage}
        onRequestClose={::this.onNotificationToastDismissed}
        autoHideDuration={TOAST_AUTOHIDE_DURATION}
        onActionTouchTap={::this.onNotificationToastActionClicked} />
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
    const skipIcon = <FontIcon className="material-icons">skip_next</FontIcon>

    return (
      <div className={style.main}>
        <QuestionPicture
          pictureId={questionPictureId}
          questionNumber={questionNumber}
          onPictureLoaded={::this.onQuestionPictureLoaded}
          requiresCalculator={questionRequiresCalculator} />
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

        {this.renderAnswerPopup(questionIsMutipleChoice)}
        {this.renderNotificationToast()}
      </div>
    )
  }
}

export default QuizTaker
