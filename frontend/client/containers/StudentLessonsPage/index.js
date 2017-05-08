
import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import actions from 'actions'

import StudentLessonsList from 'components/StudentLessonsList'
import { extractErrorFromResultingActions } from 'utils'

const fakeData = {
  currentLesson: { id: 1001, chapter: 3, section: 2, started: true, name: 'System of Linear Equations' },
  upcomingLessons: [
    { id: 1002, chapter: 3, section: 3, started: false, name: 'Equations with One Variable' },
    { id: 1003, chapter: 3, section: 4, started: false, name: 'Polynomials' },
    { id: 1004, chapter: 3, section: 5, started: false, name: 'Another One' },
    { id: 1005, chapter: 3, section: 6, started: false, name: 'Another One Pt. 2' },
  ],
}

class StudentLessonsPage extends Component {

  state = {
    loadingError:             null,
    isDataLoading:            false,
  }


  componentWillMount() {
    if (this.props.dataShouldBeLoaded) this.loadData()
  }

  loadData() {
    const {
      loadUserLessonCurrent,
      loadUserLessonUpcomings,
    } = this.props.actions

    // Indicate loading.
    this.setState({ isDataLoading: true, loadingError: null })

    // Re-load everything.
    Promise
      .all([
        loadUserLessonCurrent(),
        loadUserLessonUpcomings(),
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
    const { actions, currentUserLesson, upcomingUserLessons} = this.props
    const {
      isDataLoading
    } = this.state

    return (
      <StudentLessonsList
        currentLesson={currentUserLesson}
        upcomingLessons={upcomingUserLessons} />
    )
  }
}


const reduxify = connect(
  (state, props) => ({
    currentUserLesson:     state.currentLesson,
    upcomingUserLessons:   state.upcomingLessons,
    dataShouldBeLoaded: (
      !state.currentLessonLoaded   ||
      !state.upcomingLessonsLoaded
    ),
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.currentlesson, dispatch),
      bindActionCreators(actions.upcominglessons, dispatch))
  }))


export default reduxify(StudentLessonsPage)
