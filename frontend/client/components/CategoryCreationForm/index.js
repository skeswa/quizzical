
import ReactDOM from 'react-dom'
import TextField from 'material-ui/TextField'
import React, { Component } from 'react'

import FormError from 'components/FormError'
import FormLoader from 'components/FormLoader'

class CategoryCreationForm extends Component {
  static propTypes = {
    error:    React.PropTypes.any,
    loading:  React.PropTypes.bool.isRequired,
  }

  state = {
    code: '',
    name: '',
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

  getJSON = () => {
    const { code, name } = this.state
    return { code, name }
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
          title="Failed to create category"
          message={message} />
      )
    }

    return null
  }

  render() {
    const { loading } = this.props
    const { code, name } = this.state

    return (
      <div>
        <FormLoader visible={loading} />
        {this.renderError()}
        <TextField
          value={code}
          hintText="e.g. lineq"
          onChange={::this.onCodeChanged}
          fullWidth={true}
          floatingLabelText="Code" />
        <TextField
          value={name}
          hintText="e.g. Linear Equations"
          onChange={::this.onNameChanged}
          fullWidth={true}
          floatingLabelText="Name" />
      </div>
    )
  }
}

export default CategoryCreationForm
