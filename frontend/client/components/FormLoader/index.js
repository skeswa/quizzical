
import LinearProgress from 'material-ui/LinearProgress'
import React, { Component } from 'react'
import classNames from 'classnames'

import style from './style.css'

class FormLoader extends Component {
  static propTypes = {
    visible:    React.PropTypes.bool,
    fullScreen: React.PropTypes.bool,
  }

  state = {
    mounted: false,
    visible: false,
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.visible && !this.props.visible) {
      this.setState({ mounted: true }, () => {
        setTimeout(() => {
          this.setState({ visible: true })
        }, 300)
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
    const { fullScreen } = this.props
    const { visible, mounted } = this.state

    const className = classNames(style.main, {
      [style.main__visible]: visible,
      [style.main__mounted]: mounted,
      [style.main__fullscreen]: fullScreen,
    })

    return (
      <div className={className}>
        <LinearProgress mode="indeterminate" />
      </div>
    )
  }
}

export default FormLoader
