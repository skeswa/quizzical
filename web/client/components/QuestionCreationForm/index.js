
import React, { Component } from 'react'
import TextField from 'material-ui/TextField'

import style from './style.css'

class QuestionCreationForm extends Component {
  state = {}

  render() {
    return (
      <div className={style.main}>
        <form ref="form">
          <TextField
            name="answer"
            hintText="e.g. A...E or 123.4 or 1/2"
            floatingLabelText="Answer" />
          <TextField
            name="source"
            hintText="e.g. Barron's Book"
            floatingLabelText="Question Source" />
        </form>
      </div>
    )
  }
}

export default QuestionCreationForm
