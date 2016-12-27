
import React, { Component } from 'react'
import RaisedButton from 'material-ui/RaisedButton'

import style from './style.css'

const ANSWERS = [ 'A', 'B', 'C', 'D', 'E' ]

class MultipleChoiceAnswerer extends Component {
  static propTypes = {
    onAnswerChanged: React.PropTypes.func.isRequired
  }

  state = {
    selectedAnswer: null
  }

  onAnswerChanged(answer) {
    this.props.onAnswerChanged(answer)
    this.setState({ selectedAnswer: answer })
  }

  render() {
    return (
      <div className={style.main}>
        {
          ANSWERS.map((answer, i) => (
            <RaisedButton
              key={answer}
              value={answer}
              label={answer}
              labelColor={
                answer === this.state.selectedAnswer
                ? '#ffffff'
                : null
              }
              backgroundColor={
                answer === this.state.selectedAnswer
                ? '#754aec'
                : null
              }
              onClick={() => this.onAnswerChanged(answer)}
              fullWidth={true} />
          ))
        }
      </div>
    )
  }
}

export default MultipleChoiceAnswerer
