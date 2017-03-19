
import classNames from 'classnames'
import React, { Component, PropTypes } from 'react'

import style from './cell.css'
import {
  CELL_TYPE_ANSWER,
  CELL_TYPE_SYMBOL,
  CELL_VALUE_BLANK,
} from './constants'

class FreeResponseAnswererCell extends Component {
  static propTypes = {
    type:         React.PropTypes.string.isRequired,
    value:        React.PropTypes.string.isRequired,
    column:       React.PropTypes.number.isRequired,
    selected:     React.PropTypes.bool,
    onSelection:  React.PropTypes.func,
  }

  isDisabled(column, value) {
    return (
      (((column % 3) === 0) && (value === '/')))
  }

  onClick(e) {
    const { type, column, value, selected, onSelection } = this.props

    if (type !== CELL_TYPE_ANSWER && !this.isDisabled(column, value)) {
      onSelection(column, value)
    } else {
      e.preventDefault()
    }
  }

  render() {
    const { type, value, column, selected } = this.props

    const cellIsBlank = type === CELL_TYPE_SYMBOL
        && value === CELL_VALUE_BLANK
    const className = classNames(
      style.cell,
      style[`${type}Cell`],
      {
        [style.cell__blank]:    cellIsBlank,
        [style.cell__disabled]: this.isDisabled(column, value),
        [style.cell__selected]: selected,
      })

    return (
      <div className={className} onClick={::this.onClick}>
        <div className={style.cellLabel}>{cellIsBlank ? 'Blank' : value}</div>
      </div>
    )
  }
}

export default FreeResponseAnswererCell
