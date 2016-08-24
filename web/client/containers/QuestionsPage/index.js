
import { connect } from 'react-redux'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import style from './style.css'
import actions from 'actions'
import Questions from 'components/Questions'

const QuestionsPage = (props, context) => {
  return (
    <Questions
      actions={props.actions}
      questions={props.questions}
      categories={props.categories}
      difficulties={props.difficulties}
      dataShouldBeLoaded={props.dataShouldBeLoaded} />
  )
}

const reduxify = connect(
  (state, props) => ({
    questions:          state.question.list,
    categories:         state.category.list,
    difficulties:       state.difficulty.list,
    dataShouldBeLoaded: (
      !state.question.loaded ||
      !state.category.loaded ||
      !state.difficulty.loaded
    ),
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.question, dispatch),
      bindActionCreators(actions.category, dispatch),
      bindActionCreators(actions.difficulty, dispatch),
    )
  })
)

export default reduxify(QuestionsPage)
