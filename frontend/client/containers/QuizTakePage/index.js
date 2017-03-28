
import { connect } from 'react-redux'
import RaisedButton from 'material-ui/RaisedButton'
import { withRouter } from 'react-router'
import RefreshIndicator from 'material-ui/RefreshIndicator'
import { bindActionCreators } from 'redux'
import React, { Component, PropTypes } from 'react'

import style from './index.css'
import actions from 'actions'
import QuizTaker from 'components/QuizTaker'
import FormError from 'components/FormError'
import QuizResults from 'components/QuizResults'
import PracticeSkeleton from 'components/PracticeSkeleton'
import { extractErrorFromResultingActions } from 'utils'

class QuizTakePage extends Component {
  static propTypes = {
    match:    PropTypes.object.isRequired,
    quizzes:  PropTypes.object.isRequired,
    actions:  PropTypes.object.isRequired,
    history:  PropTypes.object.isRequired,
  }

  state = {
    quizResults:            null,
    quizFinished:           false,
    loadingError:           null,
    isDataLoading:          false,
    currentQuestionIndex:   0,
  }

  componentDidMount() {
    // Get the quiz, or exit isf there is none.
    const quiz = this.getQuiz()
    if (!quiz) {
      // Get the quiz id, or exit if there is none.
      const quizId = this.getQuizId()
      if (!quizId) {
        // Indicate that the quiz id was not specified.
        return this.setState({
          isDataLoading: false,
          loadingError: `Could not load quiz with id "${quizId}."`,
        })
      }

      // Indicate loading.
      this.setState({ isDataLoading: true, loadingError: null })

      // Load the quiz.
      this.props.actions.loadQuiz(quizId)
        .then(resultingActions => {
          const error = extractErrorFromResultingActions(resultingActions)
          if (error) {
            this.setState({
              loadingError:   error,
              isDataLoading:  false,
            })
          } else {
            this.setState({ isDataLoading: false })
          }
        })
    }
  }

  getQuiz() {
    const quizId = this.getQuizId()
    return quizId && this.props.quizzes
        ? this.props.quizzes[quizId]
        : null
  }

  getQuizId() {
    return this.props.match && this.props.match.params
        ? this.props.match.params.id
        : null
  }

  onQuizFinished(quizSubmission) {
    this.setState({
      quizFinished: true,
      isDataLoading: true,
      loadingError: null,
    })

    // Submit the quiz.
    this.props.actions.createQuizSubmission(quizSubmission)
      .then(resultingActions => {
        const error = extractErrorFromResultingActions(resultingActions)
        if (error) {
          this.setState({
            loadingError:   error,
            isDataLoading:  false,
          })
        } else {
          this.setState({
            quizResults:   resultingActions.payload,
            isDataLoading: false,
          })
        }
      })
  }

  onQuizCancelled() {
    this.setState({
      quizFinished: true,
      isDataLoading: true,
      loadingError: null,
    })
    
    // Cancel the quiz.
    this.props.actions.deleteQuizSubmission(quizSubmission)
      .then(resultingActions => {
        const error = extractErrorFromResultingActions(resultingActions)
        if (error) {
          this.setState({
            loadingError:   error,
            isDataLoading:  false,
          })
        } else {
          this.setState({
            isDataLoading: false,
          })
          this.props.history.push(`/quiz`)
        }
      })
  }

  onQuestionIndexChanged(currentQuestionIndex) {
    this.setState({ currentQuestionIndex })
  }

  renderLoading(quizFinished) {
    const participle = quizFinished ? 'being submitted' : 'loading'
    const subtitle = `Your quiz is ${participle}`

    return (
      <div className={style.main}>
        <div className={style.centerer}>
          <div className={style.loader}>
            <RefreshIndicator
              top={0}
              left={0}
              size={50}
              status="loading"
              loadingColor="#754aec" />
          </div>
        </div>
      </div>
    )
  }

  renderError(loadingError, quizFinished) {
    if (loadingError) {
      const message = loadingError.error
        ? loadingError.error
        : loadingError
      const verb = quizFinished
        ? 'submit'
        : 'load'
      const subtitle = `Failed to ${verb} the quiz`

      return (
        <PracticeSkeleton
          title={subtitle}
          action="Start Over"
          animated={true}
          animationDelay={0}
          onActionClicked={::this.onQuizCancelled}>
          <div className={style.errorWrapper}>
            <FormError
              title={subtitle}
              message={message}
              limitHeight={false} />
          </div>
        </PracticeSkeleton>
      )
    }

    return null
  }

  renderQuizTaker() {
    const quiz = this.getQuiz()
    const { currentQuestionIndex } = this.state
    const currentQuestionNumber = currentQuestionIndex + 1
    const questionTotal = quiz && quiz.questions
      ? quiz.questions.length
      : '?'
    const subtitle = `Now showing question ${currentQuestionNumber} of ` +
      `${questionTotal}`

    return (
      <div className={style.quizTakerWrapper}>
        <QuizTaker
          quiz={this.getQuiz()}
          questionIndex={currentQuestionIndex}
          onQuizFinished={::this.onQuizFinished}
          onQuizCancelled={::this.onQuizCancelled}
          onQuestionIndexChanged={::this.onQuestionIndexChanged} />
      </div>
    )
  }

  renderFinishSplash(quizResults) {
    return (
      <PracticeSkeleton
        title="Quiz Results"
        action="Start Over"
        animated={true}
        fullScreen={true}
        bodyUnpadded={true}
        animationDelay={0}
        onActionClicked={::this.onQuizCancelled}>
        <QuizResults results={quizResults} />
      </PracticeSkeleton>
    )
  }

  render() {
    const quiz = this.getQuiz()
    const {
      quizResults,
      loadingError,
      quizFinished,
      isDataLoading,
    } = this.state

    if (loadingError) return this.renderError(loadingError, quizFinished)
    if (isDataLoading || !quiz) return this.renderLoading(quizFinished)
    if (quizResults) return this.renderFinishSplash(quizResults)
    if (quiz && quiz.questions.length > 1) return this.renderQuizTaker()

    return this.renderError(
      'The generated quiz had no questions due to an unforeseen error. ' +
        'Please try again later.',
      quizFinished)
  }
}

const reduxify = connect(
  (state, props) => ({
    quizzes: state.quiz.map,
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.quiz, dispatch),
      bindActionCreators(actions.quizSubmission, dispatch))
  }))

// Connect the quiz take page to the store.
const QuizTakePageWithRedux = reduxify(QuizTakePage)
// Connect the quiz take page to the router so that it can perform history
// operations.
const QuizTakePageWithReduxWithRouter = withRouter(QuizTakePageWithRedux)

export default QuizTakePageWithReduxWithRouter
