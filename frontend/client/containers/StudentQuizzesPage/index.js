
import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import actions from 'actions'

import StudentQuizzesList from 'components/StudentQuizzesList'
import { extractErrorFromResultingActions } from 'utils'

class StudentQuizzesPage extends Component {

  state = {
    loadingError:             null,
    isDataLoading:            false,
  }


  componentWillReceiveProps() {
    if (this.props.dataShouldBeLoaded) this.loadData()
  }
  componentWillMount() {
    if (this.props.dataShouldBeLoaded) this.loadData()
  }

  loadData() {
    const {
      loadLatestQuiz,
      loadRecentQuizzes,
      loadUnsubmittedQuizzes,
    } = this.props.actions

    // Indicate loading.
    this.setState({ isDataLoading: true, loadingError: null })

    // Re-load everything.
    Promise
      .all([
        loadLatestQuiz(),
        loadRecentQuizzes(),
        loadUnsubmittedQuizzes(),
      ])
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

  render() {
    const {
      actions,
      latestUserQuiz,
      recentUserQuizzes,
      unsubmittedUserQuizzes,
    } = this.props

    const {
      isDataLoading
    } = this.state

    return (
        <StudentQuizzesList
        latestQuiz={latestUserQuiz}
        recentQuizzes={recentUserQuizzes}
        unsubmittedQuizzes={unsubmittedUserQuizzes} />
    )
  }
}


const reduxify = connect(
  (state, props) => ({
    latestUserQuiz:     state.userQuiz.latestQuiz,
    recentUserQuizzes:   state.userQuiz.recentQuizzes,
    unsubmittedUserQuizzes:   state.userQuiz.unsubmittedQuizzes,
    dataShouldBeLoaded: (
      !state.userQuiz.latestQuizLoaded   ||
      !state.userQuiz.recentQuizzesLoaded ||
      !state.userQuiz.unsubmittedQuizzesLoaded
    ),
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.userQuiz, dispatch)),
  }))


export default reduxify(StudentQuizzesPage)
