
import LinearProgress from 'material-ui/LinearProgress'
import React, { Component } from 'react'
import classNames from 'classnames'

import style from './style.css'

class FormLoader extends Component {
  state = {
    mounted: false,
    visible: false,
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.visible && !this.props.visible) {
      this.setState({ mounted: true }, () => {
        this.setState({ visible: true })
      })
    } else if (!nextProps.visible && this.props.visible) {
      this.setState({ visible: false }, () => {
        setTimeout(() => {
          this.setState({ mounted: false })
        }, 300)
      })
    }
  }

  render() {
    const { visible, mounted } = this.state

    const className = classNames(style.main, {
      [style.main__visible]: visible,
      [style.main__mounted]: mounted,
    })

    return (
      <div className={className}>
        <LinearProgress mode="indeterminate" />
      </div>
    )
  }
}

export default FormLoader
