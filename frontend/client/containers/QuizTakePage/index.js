
import { connect } from 'react-redux'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import actions from 'actions'
import QuizTaker from 'components/QuizTaker'
import PracticeSkeleton from 'components/PracticeSkeleton'

class QuizTakePage extends Component {
  render() {
    const { params, quizzes, dataShouldBeLoaded } = this.props
    const quizId = params ? params.quizId : null
    const quiz = quizId && quizzes ? quizzes[quizId] : null

    return (
      <PracticeSkeleton title="Quiz Taker" subtitle="Question 1 of 1">
        <QuizTaker quiz={quiz} dataShouldBeLoaded={dataShouldBeLoaded} />
      </PracticeSkeleton>
    )
  }
}

const reduxify = connect(
  (state, props) => ({
    quizzes:            state.quiz.list,
    dataShouldBeLoaded: !state.quiz.loaded,
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.quiz, dispatch))
  })
)

export default reduxify(QuizTakePage)
