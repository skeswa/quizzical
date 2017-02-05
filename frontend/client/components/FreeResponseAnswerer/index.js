
import classNames from 'classnames'
import React, { Component } from 'react'

import style from './style.css'

const CELL_TYPE_ANSWER = 'answer'
const CELL_TYPE_SYMBOL = 'symbol'
const CELL_TYPE_NUMBER = 'number'

const SYMBOL_CELL_VALUES = [ '', '/', '.' ]
const NUMBER_CELL_VALUES = [ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' ]

class FreeResponseAnswerer extends Component {
  static propTypes = {
    onAnswerChanged: React.PropTypes.func.isRequired
  }

  state = {
    answer: [ '', '', '', '' ],
  }

  onCellSelection(columnIndex, value) {
    const newAnswer = this.state.answer.slice(0)
    newAnswer[columnIndex] = value

    this.setState({ answer: newAnswer })
    this.props.onAnswerChanged(newAnswer.join(''))
  }

  renderColumn(index) {
    const { answer } = this.state
    const symbolCells = SYMBOL_CELL_VALUES
      .map(value =>
        <Cell
          key={value}
          type={CELL_TYPE_SYMBOL}
          value={value}
          column={index}
          selected={value === answer[index]}
          onSelection={::this.onCellSelection} />)
    const numberCells = NUMBER_CELL_VALUES
      .map(value =>
        <Cell
          key={value}
          type={CELL_TYPE_NUMBER}
          value={value}
          column={index}
          selected={value === answer[index]}
          onSelection={::this.onCellSelection} />)

    return (
      <div className={style.column}>
        <Cell
          type={CELL_TYPE_ANSWER}
          value={answer[index]}
          column={index} />
        <div className={style.symbolCells}>{symbolCells}</div>
        <div className={style.numberCells}>{numberCells}</div>
      </div>
    )
  }

  render() {
    return (
      <div className={style.main}>
        <div className={style.columns}>
          {this.renderColumn(0)}
          {this.renderColumn(1)}
          {this.renderColumn(2)}
          {this.renderColumn(3)}
        </div>
      </div>
    )
  }
}

class Cell extends Component {
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
    const className = classNames(
      style.cell,
      style[`${type}Cell`],
      {
        [style.cell__blank]:    !value,
        [style.cell__disabled]: this.isDisabled(column, value),
        [style.cell__selected]: selected,
      })

    return (
      <div className={className} onClick={::this.onClick}>
        <div className={style.cellLabel}>
          { (value || type === CELL_TYPE_ANSWER)  ? value : 'Blank' }
        </div>
      </div>
    )
  }
}

export default FreeResponseAnswerer
