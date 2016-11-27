
import ReactDOM from 'react-dom'
import TextField from 'material-ui/TextField'
import React, { Component } from 'react'

import FormError from 'components/FormError'
import FormLoader from 'components/FormLoader'

class DifficultyCreationForm extends Component {
  static propTypes = {
    error:    React.PropTypes.any,
    loading:  React.PropTypes.bool.isRequired,
  }

  state = {
    code: '',
    name: '',
    color: '',
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.error !== nextProps.error) {
      // Make sure that the error is visible.
      this.scrollToTop()
    }
  }

  onCodeChanged(e) {
    this.setState({ code: e.target.value })
  }

  onNameChanged(e) {
    this.setState({ name: e.target.value })
  }

  onColorChanged(e) {
    this.setState({ color: e.target.value })
  }

  getJSON = () => {
    const { code, name, color } = this.state
    return { code, name, color }
  }

  scrollToTop() {
    const node = ReactDOM.findDOMNode(this)
    const parentNode = node.parentNode
    parentNode.scrollTop = 0
  }

  renderError() {
    const { error } = this.props

    if (error) {
      const message = error.error
        ? error.error
        : error

      return (
        <FormError
          title="Failed to create difficulty"
          message={message} />
      )
    }

    return null
  }

  render() {
    const { loading } = this.props
    const { code, name, color } = this.state

    return (
      <div>
        <FormLoader visible={loading} />
        {this.renderError()}
        <TextField
          value={code}
          hintText="e.g. medium"
          onChange={::this.onCodeChanged}
          fullWidth={true}
          floatingLabelText="Code" />
        <TextField
          value={name}
          hintText="e.g. Medium"
          onChange={::this.onNameChanged}
          fullWidth={true}
          floatingLabelText="Name" />
        <TextField
          value={color}
          hintText="e.g. #ff0000"
          onChange={::this.onColorChanged}
          fullWidth={true}
          floatingLabelText="Color" />
      </div>
    )
  }
}

export default DifficultyCreationForm
