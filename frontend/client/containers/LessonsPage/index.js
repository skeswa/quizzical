
import { connect } from 'react-redux'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import style from './style.css'
import actions from 'actions'
import Lessons from 'components/Lessons'
import AdminSkeleton from 'components/AdminSkeleton'

const LessonsPage = (props, context) => {
  return (
    <AdminSkeleton>
      <Lessons
        actions={props.actions}
        sources={props.sources}
        lessons={props.userLessons}
        categories={props.categories}
        difficulties={props.difficulties}
        dataShouldBeLoaded={props.dataShouldBeLoaded} />
    </AdminSkeleton>
  )
}

const reduxify = connect(
  (state, props) => ({
    sources:            state.source.list,
    userLessons:        state.userLesson.list,
    categories:         state.category.list,
    difficulties:       state.difficulty.list,
    dataShouldBeLoaded: (
      !state.source.loaded ||
      !state.userLesson.loaded ||
      !state.category.loaded ||
      !state.difficulty.loaded
    ),
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.source, dispatch),
      bindActionCreators(actions.userLesson, dispatch),
      bindActionCreators(actions.category, dispatch),
      bindActionCreators(actions.difficulty, dispatch),
    )
  })
)

export default reduxify(LessonsPage)
