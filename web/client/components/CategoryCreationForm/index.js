
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
    name: '',
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.error !== nextProps.error) {
      // Make sure that the error is visible.
      this.scrollToTop()
    }
  }

  onNameChanged(e) {
    this.setState({ name: e.target.value })
  }

  getJSON = () => {
    return { name: this.state.name }
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
    const { name, color } = this.state

    return (
      <div>
        <FormLoader visible={loading} />
        {this.renderError()}
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
