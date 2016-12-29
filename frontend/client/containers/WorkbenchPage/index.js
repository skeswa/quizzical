
import { connect } from 'react-redux'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import actions from 'actions'
import FreeResponseAnswerer from 'components/FreeResponseAnswerer'

const WorkbenchPage = (props, context) => {
  return (
    <div style={{
      width: '100%',
      height: '100%',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: '#ddd',
    }}>
      <div style={{
        width: '309px',
        height: '420px',
        padding: '20px',
        boxShadow: '0 2px 8px rgba(0, 0, 0, 0.4)',
        borderRadius: '4px',
      }}>
        <FreeResponseAnswerer
          onAnswerChanged={answer => console.log('answer', answer)} />
      </div>
    </div>
  )
}

const reduxify = connect(
  (state, props) => ({}),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      /* bindActionCreators(actions.source, dispatch) */)
  })
)

export default reduxify(WorkbenchPage)
