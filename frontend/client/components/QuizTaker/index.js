
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
          ? this.setState({ visible: true })
          : null,
      300)
  }

  componentWillReceiveProps(nextProps) {
    const { responses } = this.state
    const nextQuestionIndex = nextProps.questionIndex
    const currentQuestionIndex = this.props.questionIndex

    // If the question index changes, set the current answer to whatever
    // response answer we already have in memory.
    if (nextQuestionIndex
        && (nextQuestionIndex !== currentQuestionIndex)
        && responses[nextQuestionIndex]) {
      this.setState({ currentAnswer: responses[nextQuestionIndex].answer })
    }
  }

  componentWillUnmount() {
    this.mounted = false
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
    for (let i = 0; i < questions.length - 1; i++) {
      const index = (initialIndex + i + 1) % questions.length
      const response = responses[index]

      if (!response) {
        return index
      }

      if (firstSkippedIndex === null && response.skipped) {
        firstSkippedIndex = index
      }
    }

    return firstSkippedIndex
  }

  respondToCurrentQuestion(skip, report) {
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

    // TODO(skeswa): handle "this is the last remaining question" case with a
    // popup instead of assuming they meant to finish the quiz.
    const nextQuestionIndex =
        this.findNextUnattemptedQuestionIndex(
            questionIndex,
            questions,
            responses)

    this.setState(
      {
        responses: Object.assign(
          {},
          responses,
          {
            [questionIndex]: {
              answer: skip ? null : currentAnswer,
              skipped: !!skip,
              reported: !!report,
              secondsElapsed: timeCurrentQuestionStarted !== null
                  ? (Date.now() - timeCurrentQuestionStarted) / 1000
                  : 0,
            },
          }),
        currentAnswer: null,
        answerPopupVisible: false,
        questionsAttempted: responses[questionIndex]
            ? questionsAttempted
            : questionsAttempted + 1,
        timeCurrentQuestionStarted: null,
      },
      () => {
        nextQuestionIndex === null
            ? onQuizFinished(this.composeSubmission())
            : onQuestionIndexChanged(nextQuestionIndex)
      })
  }

  onAnswerChanged(currentAnswer) {
    this.setState({ currentAnswer })
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
    this.setState({
      timeCurrentQuestionStarted: Date.now(),
    })
  }

  onCurrentQuestionIndexChanged() {
    // TODO
  }

  render() {
    const { questionIndex, quiz: { questions } } = this.props
    const { visible, responses, currentAnswer, questionsAttempted } = this.state

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
          <QuizTakerHeader
            questionIndex={questionIndex}
            questionTotal={questions.length}
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
                style={{ background: '#fff', flex: '1 0' }}
                questionId={questionId}
                pictureId={questionPictureId}
                onPictureLoaded={::this.onQuestionPictureLoaded} />
            </div>
            <div className={style.bottomRight}>
              <QuizTakerAnswerPanel
                answer={currentAnswer}
                questionTotal={questions.length}
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
