
import FontIcon from 'material-ui/FontIcon'
import classNames from 'classnames'
import RefreshIndicator from 'material-ui/RefreshIndicator'
import React, { Component } from 'react'

import style from './index.css'
import QuizTakerHeader from './header'
import QuestionPicture from 'components/QuestionPicture'
import QuizTakerAnswerPanel from './answer-panel'
import FreeResponseAnswerer from 'components/FreeResponseAnswerer'
import QuizTakerQuestionPager from './question-pager'
import MultipleChoiceAnswerer from 'components/MultipleChoiceAnswerer'

const SKIP_BUTTON_STYLE = { width: '3.6rem', minWidth: '1.5rem' }
const QUESTION_PICTURE_STYLE = { background: '#fff', flex: '1 0' }

class QuizTaker extends Component {
  static propTypes = {
    quiz:                   React.PropTypes.object.isRequired,
    questionIndex:          React.PropTypes.number.isRequired,
    onQuizFinished:         React.PropTypes.func.isRequired,
    onQuizCancelled:        React.PropTypes.func.isRequired,
    onQuestionIndexChanged: React.PropTypes.func.isRequired,
  }

  state = {
    visible:                      false,
    responses:                    {},
    quizFinalized:                false,
    currentAnswer:                null,
    lightboxMounted:              false,
    lightboxVisible:              false,
    questionsAttempted:           0,
    timeCurrentQuestionStarted:   null,
  }
  mounted   = false
  animating = false

  componentDidMount() {
    this.mounted = true

    setTimeout(() =>
      this.mounted
          ? this.setState(
              { visible: true },
              () => this.startNextQuestion(
                this.props.questionIndex,
                this.state.responses))
          : null,
      300)
  }

  componentWillReceiveProps(nextProps) {
    const { responses } = this.state
    const nextQuestionIndex = nextProps.questionIndex
    const currentQuestionIndex = this.props.questionIndex

    if (nextQuestionIndex !== currentQuestionIndex) {
      // When the index is changed by the parent, advance to the designated
      // question.
      this.startNextQuestion(nextQuestionIndex, responses)
    }
  }

  componentWillUnmount() {
    this.mounted = false
  }

  startNextQuestion(nextQuestionIndex, responses) {
    if (Number.isInteger(nextQuestionIndex)) {
      const {
        answer = null,
        secondsElapsed = 0,
      } = responses[nextQuestionIndex] || {}

      this.setState({
        currentAnswer: answer,
        timeCurrentQuestionStarted:
            Date.now() - Math.round(secondsElapsed * 1000),
      })
    }
  }

  composeSubmission() {
    const { quiz: { id: quizId, questions } } = this.props
    const { responses } = this.state

    if (!questions || !responses) {
      return { quizId, responses: [] }
    }

    // TODO(skeswa): handle the case where the timer updates.
    return {
      quizId,
      responses: questions.map((question, i) => {
        const {
          answer = null,
          skipped = true,
          reported = false,
          secondsElapsed = 0,
        } = responses[i] || {}
        const { id: quizProblemId } = question

        return {
          skipped,
          reported,
          quizProblemId,

          response: answer,
          secondsElapsed: Math.round(secondsElapsed),
        }
      }),
    }
  }

  findNextUnattemptedQuestionIndex(
    initialIndex,
    questions,
    responses,
  ) {
    let firstSkippedIndex = null

    // Find the next acceptable question by finding the next unattempted
    // question, or, if one does not exist, find the next skipped question.
    for (let i = 0; i < questions.length - 1; i++) {
      const index = (initialIndex + i + 1) % questions.length
      const response = responses[index]

      if (!response) {
        return index
      }

      // Look for both skipped AND nor reported (duh).
      if (firstSkippedIndex === null
          && response.skipped
          && !response.reported) {
        firstSkippedIndex = index
      }
    }

    // If there weren't any other good questions to visit, just stick to the
    // current one.
    return firstSkippedIndex !== null
        ? firstSkippedIndex
        : initialIndex
  }

  respondToCurrentQuestion(skip, report, nextQuestionIndex) {
    const {
      responses,
      currentAnswer,
      questionsAttempted,
      timeCurrentQuestionStarted,
    } = this.state

    const {
      quiz: { questions },

      questionIndex,
      onQuizFinished,
      onQuestionIndexChanged,
    } = this.props

    // Figure out where we are headed next if it wasn't specified.
    const nextQuestionIndexDefined = Number.isInteger(nextQuestionIndex)
    if (!nextQuestionIndexDefined) {
      nextQuestionIndex =
          this.findNextUnattemptedQuestionIndex(
              questionIndex,
              questions,
              responses)
    }

    // Calculate shanges to common state variables.
    const onLastQuestion = (questionIndex === nextQuestionIndex)
    const existingResponse = responses[questionIndex]
    const shouldSkipQuestion =
        // Only skip the question if its not the student paging through already
        // answered questions.
        skip && !(
          nextQuestionIndexDefined
          && existingResponse
          && existingResponse.answer)
    const updatedResponses = Object.assign(
      {},
      responses,
      {
        [questionIndex]: {
          answer: shouldSkipQuestion ? null : currentAnswer,
          skipped: shouldSkipQuestion,
          reported: !!report,
          secondsElapsed: timeCurrentQuestionStarted !== null
              ? (Date.now() - timeCurrentQuestionStarted) / 1000
              : 0,
        },
      })
    const updatedQuestionsAttempted =
        responses[questionIndex]
            ? questionsAttempted
            : questionsAttempted + 1

    if (onLastQuestion) {
      this.setState({
        responses: updatedResponses,
        questionsAttempted: updatedQuestionsAttempted,

        // Assert that the quiz is tentatively finalized since the last question
        // has a response.
        quizFinalized: true,
      })
      return
    }

    this.setState(
      {
        responses: updatedResponses,
        currentAnswer: null,
        questionsAttempted: updatedQuestionsAttempted,
      },
      () => onQuestionIndexChanged(nextQuestionIndex))
  }

  onQuizFinished() {
    const { onQuizFinished } = this.props

    this.setState({ visible: false }, () => {
      setTimeout(() => onQuizFinished(this.composeSubmission()), 500)
    })
  }

  onAnswerChanged(currentAnswer) {
    // Set the current answer, and, since the quiz data now could potentially
    // change, mark the quiz un-finalized.
    this.setState({ currentAnswer, quizFinalized: false })
  }

  onAnswerSubmitted() {
    this.respondToCurrentQuestion(false /* skip */, false /* report */)
  }

  onQuestionSkipped() {
    this.respondToCurrentQuestion(true /* skip */, false /* report */)
  }

  onQuestionReported() {
    this.respondToCurrentQuestion(true /* skip */, true /* report */)
  }

  onCurrentQuestionIndexChanged(nextQuestionIndex) {
    this.respondToCurrentQuestion(
      true /* skip */,
      false /* report */,
      nextQuestionIndex)
  }

  onQuestionClicked() {
    if (this.animating) return;

    this.animating = true
    this.setState(
      { lightboxMounted: true },
      () => setTimeout(
        () => {
          if (this.mounted) {
            this.animating = false
            this.setState({ lightboxVisible: true })
          }
        },
        300))
  }

  onQuestionPictureLoaded() {
    // TODO(skeswa): do some sort of timer discounting here in future. We should
    // not be penalizing the student for a slow image load time.
  }

  render() {
    const { quiz: { questions }, questionIndex } = this.props

    const {
      visible,
      responses,
      currentAnswer,
      quizFinalized,
      questionsAttempted,
      timeCurrentQuestionStarted,
    } = this.state

    const question = questions[questionIndex]
    if (!question) {
      return (
        <div className={classNames(style.main, style.main__visible)} />
      )
    }

    const {
      problem: {
        id:                     questionId,
        multipleChoice:         questionIsMutipleChoice,
        questionPicture:        { id: questionPictureId },
        requiresCalculator:     questionRequiresCalculator,
        sourceIndexWithinPage:  questionNumber,
      },
    } = question
    const skipIcon = <FontIcon className="material-icons">skip_next</FontIcon>
    const mainClassName = classNames(style.main, {
      [style.main__visible]: visible,
    })

    return (
      <div className={mainClassName}>
        <div className={style.top}>
          {/* TODO(skeswa): set the time limit dynamically for different questions */}
          <QuizTakerHeader
            questionIndex={questionIndex}
            questionTotal={questions.length}
            questionTimeLimit={120}
            timeQuestionStarted={timeCurrentQuestionStarted}
            questionNumberInPage={questionNumber} />
        </div>
        <div className={style.bottomOuter}>
          <div className={style.bottomInner}>
            <div className={style.bottomLeft}>
              <QuizTakerQuestionPager
                questions={questions}
                responses={responses}
                currentQuestionIndex={questionIndex}
                onCurrentQuestionIndexChanged={
                  ::this.onCurrentQuestionIndexChanged
                } />
            </div>
            <div className={style.bottomMiddle}>
              <QuestionPicture
                style={QUESTION_PICTURE_STYLE}
                pictureId={questionPictureId}
                questionId={questionId}
                onPictureLoaded={::this.onQuestionPictureLoaded} />
            </div>
            <div className={style.bottomRight}>
              <QuizTakerAnswerPanel
                answer={currentAnswer}
                questionTotal={questions.length}
                quizFinalized={quizFinalized}
                onQuizFinished={::this.onQuizFinished}
                onAnswerChanged={::this.onAnswerChanged}
                onAnswerSubmitted={::this.onAnswerSubmitted}
                onQuestionSkipped={::this.onQuestionSkipped}
                onQuestionReported={::this.onQuestionReported}
                questionsAttempted={questionsAttempted}
                questionIsMutipleChoice={questionIsMutipleChoice} />
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default QuizTaker
