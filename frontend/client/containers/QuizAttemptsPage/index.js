
import { connect } from 'react-redux'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import actions from 'actions'
import QuizAttempts from 'components/QuizAttempts'
import AdminSkeleton from 'components/AdminSkeleton'

const QuizAttemptsPage = (props, context) => {
  return (
    <AdminSkeleton>
      <QuizAttempts
        actions={props.actions}
        categories={props.categories}
        quizAttempts={props.quizAttempts}
        dataShouldBeLoaded={props.dataShouldBeLoaded} />
    </AdminSkeleton>
  )
}

const reduxify = connect(
  (state, props) => ({
    categories:         state.category.list,
    quizAttempts:       state.quizAttempt.list,
    dataShouldBeLoaded: !state.quizAttempt.loaded || !state.category.loaded,
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.quiz, dispatch),
      bindActionCreators(actions.category, dispatch),
      bindActionCreators(actions.quizAttempt, dispatch),
    )
  })
)

export default reduxify(QuizAttemptsPage)
