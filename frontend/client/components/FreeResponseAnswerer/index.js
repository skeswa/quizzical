
import autobind from 'autobind-decorator'
import classNames from 'classnames'
import React, { Component, PropTypes } from 'react'

import style from './index.css'
import FreeResponseAnswererCell from './cell'

import {
  CELL_TYPE_ANSWER,
  CELL_TYPE_SYMBOL,
  CELL_TYPE_NUMBER,
  CELL_VALUE_BLANK,
  SYMBOL_CELL_VALUES,
  NUMBER_CELL_VALUES,
} from './constants'

class FreeResponseAnswerer extends Component {
  static propTypes = {
    answer:           PropTypes.string,
    onAnswerChanged:  PropTypes.func.isRequired,
  }

  formatAnswer(answer) {
    const formattedAnswer = [
      CELL_VALUE_BLANK,
      CELL_VALUE_BLANK,
      CELL_VALUE_BLANK,
      CELL_VALUE_BLANK,
    ]

    if (!answer) {
      return formattedAnswer
    }

    for (let i = 0; i < answer.length && i < formattedAnswer.length; i++) {
      formattedAnswer[i] = answer[i]
    }

    return formattedAnswer
  }

  @autobind
  onCellSelection(columnIndex, value) {
    const answer = this.formatAnswer(this.props.answer)
    answer[columnIndex] = value
    this.props.onAnswerChanged(answer.join(''))
  }

  renderColumn(index, answer) {
    const symbolCells = SYMBOL_CELL_VALUES
      .map(value =>
        <FreeResponseAnswererCell
          key={value}
          type={CELL_TYPE_SYMBOL}
          value={value}
          column={index}
          selected={value === answer[index]}
          onSelection={this.onCellSelection} />)
    const numberCells = NUMBER_CELL_VALUES
      .map(value =>
        <FreeResponseAnswererCell
          key={value}
          type={CELL_TYPE_NUMBER}
          value={value}
          column={index}
          selected={value === answer[index]}
          onSelection={this.onCellSelection} />)

    return (
      <div className={style.column}>
        <FreeResponseAnswererCell
          type={CELL_TYPE_ANSWER}
          value={answer[index]}
          column={index} />
        <div className={style.symbolCells}>{symbolCells}</div>
        <div className={style.numberCells}>{numberCells}</div>
      </div>
    )
  }

  render() {
    const answer = this.formatAnswer(this.props.answer)

    return (
      <div className={style.main}>
        <div className={style.columns}>
          {this.renderColumn(0, answer)}
          {this.renderColumn(1, answer)}
          {this.renderColumn(2, answer)}
          {this.renderColumn(3, answer)}
        </div>
      </div>
    )
  }
}

export default FreeResponseAnswerer
