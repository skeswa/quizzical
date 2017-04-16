
import autobind from 'autobind-decorator'
import classNames from 'classnames'
import React, { Component } from 'react'

import style from './style.css'

const FADE_DURATION = 300 // In milliseconds.

class Lightbox extends Component {
  static propTypes = {
    visible:      React.PropTypes.bool.isRequired,
    pictureURL:   React.PropTypes.string.isRequired,
    onDismissal:  React.PropTypes.func.isRequired,
  }

  state = {
    mounted: false,
    visible: false,
  }
  componentMounted = false

  componentDidMount() {
    this.componentMounted = true
  }

  componentWillUnmount() {
    this.componentMounted = false
  }

  componentWillReceiveProps(nextProps) {
    const { visible: nextVisible } = nextProps
    const { visible: currentVisible } = this.props

    if (currentVisible !== nextVisible) {
      if (nextVisible && !this.state.visible) {
        if (!this.componentMounted) return

        return this.setState(
          { mounted: true },
          () => {
            if (!this.componentMounted) return

            return this.setState({ visible: true })
          })
      }

      if (!nextVisible && this.state.visible) {
        if (!this.componentMounted) return

        return this.setState(
          { visible: false },
          () => setTimeout(
            () => {
              if (!this.componentMounted) return

              return this.setState({ mounted: false })
            },
            FADE_DURATION))
      }
    }
  }

  @autobind
  onClick() {
    const { onDismissal } = this.props

    if (onDismissal) onDismissal()
  }

  render() {
    const { pictureURL } = this.props
    const { mounted, visible } = this.state

    const className = classNames(
      style.lightbox,
      {
        [style.lightbox__mounted]: mounted,
        [style.lightbox__visible]: visible,
      })
    const pictureStyle = { backgroundImage: `url(${pictureURL})` }

    return (
      <div className={className} onClick={this.onClick}>
        <div style={pictureStyle} className={style.picture} />
      </div>
    )
  }
}

export default Lightbox
