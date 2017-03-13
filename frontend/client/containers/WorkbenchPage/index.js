
import { connect } from 'react-redux'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import actions from 'actions'
import QuizLengthPager from 'components/QuizLengthPager'

const QUIZ_LENGTH_NAMES = [
  'Very Short',
  'Short',
  'Medium',
  'Long',
  'Very Long',
  'Full Size'
]

class WorkbenchPage extends Component {
  state = {
    selectedLengthIndex: 1,
  }

  render() {
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
          display: 'flex',
          width: '420px',
          height: '420px',
          boxShadow: '0 2px 8px rgba(0, 0, 0, 0.4)',
          borderRadius: '4px',
        }}>
          <QuizLengthPager
            lengths={QUIZ_LENGTH_NAMES}
            selectedLengthIndex={this.state.selectedLengthIndex}
            onSelectedLengthChanged={selectedLengthIndex => this.setState({ selectedLengthIndex })} />
        </div>
      </div>
    )
  }
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
