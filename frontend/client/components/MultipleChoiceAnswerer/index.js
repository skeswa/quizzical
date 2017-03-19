
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component, PropTypes } from 'react'

import style from './style.css'

const CHOICES = [ 'A', 'B', 'C', 'D', 'E' ]

class MultipleChoiceAnswerer extends Component {
  static propTypes = {
    answer:           PropTypes.string,
    onAnswerChanged:  PropTypes.func.isRequired,
  }

  onAnswerChanged(answer) {
    this.props.onAnswerChanged(answer)
  }

  render() {
    const { answer } = this.props

    return (
      <div className={style.main}>
        {
          CHOICES.map((choice, i) => (
            <RaisedButton
              key={choice}
              label={choice}
              labelColor={
                choice === answer
                    ? '#ffffff'
                    : null
              }
              backgroundColor={
                choice === answer
                    ? '#754aec'
                    : null
              }
              onClick={this.onAnswerChanged.bind(this, choice)}
              fullWidth={true} />
          ))
        }
      </div>
    )
  }
}

export default MultipleChoiceAnswerer
