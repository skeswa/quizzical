
import { connect } from 'react-redux'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import actions from 'actions'
import QuizAttempts from 'components/QuizAttempts'
import AdminSkeleton from 'components/AdminSkeleton'

import { extractErrorFromResultingActions } from 'utils'


class QuizAttemptsPage extends Component {
  static propTypes = {
    actions:            React.PropTypes.object.isRequired,
    quizAttempts:       React.PropTypes.array.isRequired,
    dataShouldBeLoaded: React.PropTypes.bool.isRequired,
  }

  static contextTypes = {
    router: React.PropTypes.object.isRequired,
  }

  state = {
    loadingError:             null,
    isDataLoading:            false,
    quizGenerationError:      null,
    quizGenerationInProgress: false,
  }

  componentWillMount() {
    if (this.props.dataShouldBeLoaded) this.loadData()
  }

  loadData() {
    const { loadQuizzes } = this.props.actions

    // Indicate loading.
    this.setState({ isDataLoading: true, loadingError: null })

    // Re-load everything.
    loadQuizzes()
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

  onStartClicked() {
    this.setState({
      quizGenerationError:      null,
      quizGenerationInProgress: true,
    })

    const formData = this.refs.generationForm.getJSON()
    this.props.actions.generateQuiz(formData)
      .then(resultingAction => {
        if (resultingAction.error) {
          this.setState({
            quizGenerationError:      resultingAction.payload,
            quizGenerationInProgress: false,
          })
        } else {
          this.setState({
            quizGenerationInProgress:     false,
            quizGenerationDialogVisible:  false,
          })

          this.context.router.push(`/quiz/${resultingAction.payload.id}/take`)
        }
      })
  }

  render() {
    const { actions, categories } = this.props
    const {
      isDataLoading,
      quizGenerationError,
      quizGenerationInProgress,
    } = this.state

    return (
      <AdminSkeleton>
        <QuizAttempts
          actions={props.actions}
          categories={props.categories}
          quizAttempts={props.quizzes}
          dataShouldBeLoaded={props.dataShouldBeLoaded} />
      </AdminSkeleton>
    )
  }
}

const reduxify = connect(
  (state, props) => ({
    categories:         state.category.list,
    quizAttempts:       state.quiz.list,
    dataShouldBeLoaded: !state.quiz.loaded || !state.category.loaded,
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.quiz, dispatch),
      bindActionCreators(actions.category, dispatch),
    )
  })
)

export default reduxify(QuizAttemptsPage)
