
import classNames from 'classnames'
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component, PropTypes } from 'react'

import style from './index.css'

const ACTION_LABEL_STYLE = {
  fontWeight: 500,
  fontSize: '1.6rem',
  letterSpacing: '.05rem',
}

class PracticeSkeleton extends Component {
  static propTypes = {
    title:            PropTypes.string,
    action:           PropTypes.string,
    animated:         PropTypes.bool,
    actionDisabled:   PropTypes.bool,
    animationDelay:   PropTypes.number,
    onActionClicked:  PropTypes.func,
  }

  state = {
    hiddenIfAnimated: true,
  }

  componentDidMount() {
    const { animated, animationDelay } = this.props

    if (animated) {
      setTimeout(
        () => this.setState({ hiddenIfAnimated: false }),
        animationDelay ? /* default delay */ 200 : animationDelay)
    }
  }

  onActionClicked() {
    if (this.props.onActionClicked) this.props.onActionClicked()
  }

  renderFooter(action, actionDisabled) {
    return action
        ? <div className={style.cardFooter}>
            <RaisedButton
              label={action}
              primary={true}
              onClick={this.onActionClicked.bind(this)}
              disabled={actionDisabled}
              fullWidth={true}
              labelStyle={ACTION_LABEL_STYLE} />
          </div>
        : null
  }

  render() {
    const { hiddenIfAnimated } = this.state
    const { title, action, animated, children, actionDisabled } = this.props

    const cardClassName = classNames(style.card, {
      [style.card__hidden]: (hiddenIfAnimated && animated),
    })

    return (
      <div className={style.main}>
        <div className={cardClassName}>
          <div className={style.cardHeader}>
            <div className={style.cardHeaderIcon}></div>
            <div className={style.cardHeaderTitle}>{title}</div>
          </div>
          <div className={style.cardBody}>{children}</div>

          {this.renderFooter(action, actionDisabled)}
        </div>
      </div>
    )
  }
}

export default PracticeSkeleton
