
import { connect } from 'react-redux'
import RefreshIndicator from 'material-ui/RefreshIndicator'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import style from './style.css'
import actions from 'actions'
import QuizTaker from 'components/QuizTaker'
import FormError from 'components/FormError'
import PracticeSkeleton from 'components/PracticeSkeleton'
import { extractErrorFromResultingActions } from 'utils'

class QuizTakePage extends Component {
  state = {
    loadingError: null,
    isDataLoading: false,
    currentQuestionIndex: 0,
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
    return quizId && this.props.quizzes ? this.props.quizzes[quizId] : null
  }

  getQuizId() {
    return this.props.params
      ? this.props.params.quizId
      : null
  }

  onQuestionIndexChanged(currentQuestionIndex) {
    this.setState({ currentQuestionIndex })
  }

  renderLoading() {
    return (
      <PracticeSkeleton title="Quizzical" subtitle="Your quiz is loading">
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
      </PracticeSkeleton>
    )
  }

  renderError() {
    const { loadingError } = this.state

    if (loadingError) {
      const message = loadingError.error
        ? loadingError.error
        : loadingError

      return (
        <PracticeSkeleton
            title="Quizzical"
            subtitle="Failed to load the quiz">
          <div className={style.centerer}>
            <FormError
              title="Failed to load the quiz"
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
      <PracticeSkeleton title="Quiz in Progress" subtitle={subtitle}>
        <QuizTaker
          quiz={this.getQuiz()}
          questionIndex={currentQuestionIndex}
          onQuestionIndexChanged={::this.onQuestionIndexChanged} />
      </PracticeSkeleton>
    )
  }

  render() {
    const quiz = this.getQuiz()
    const { loadingError, isDataLoading } = this.state

    if (loadingError) {
      return this.renderError()
    }

    if (isDataLoading || !quiz) {
      return this.renderLoading()
    }

    return this.renderQuizTaker()
  }
}

const reduxify = connect(
  (state, props) => ({
    quizzes: state.quiz.map,
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.quiz, dispatch))
  }))

export default reduxify(QuizTakePage)
