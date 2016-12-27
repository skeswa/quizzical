
import classNames from 'classnames'
import React, { Component } from 'react'

import style from './style.css'

class FreeResponseAnswerer extends Component {
  static propTypes = {
    onAnswerChanged: React.PropTypes.func.isRequired
  }

  state = {
    answer: [ '', '', '', '' ],
  }

  onAnswerChanged() {
    const { answer } = this.state
    this.props.onAnswerChanged(answer.join(''))
  }

  renderColumn(index) {
    const { answer } = this.state

    return (
      <div className={style.column}>
        <div className={style.answerCell}>{answer[index]}</div>
        <div className={style.punctuationCells}>
          <div className={classNames(style.punctuationCell, {
            [style.punctuationCell__visible]: index % 3 > 0
          })}>
            <Button value="/" columnIndex={index} />
          </div>
          <div className={style.punctuationCell}>
            <Button value="." columnIndex={index} />
          </div>
        </div>
        <div className={style.numberCells}>
          <div className={classNames(style.numberCell, {
            [style.numberCell__visible]: index > 0
          })}>
            <Button value="0" columnIndex={index} />
          </div>
          <div className={style.numberCell}>
            <Button value="1" columnIndex={index} />
          </div>
          <div className={style.numberCell}>
            <Button value="2" columnIndex={index} />
          </div>
          <div className={style.numberCell}>
            <Button value="3" columnIndex={index} />
          </div>
          <div className={style.numberCell}>
            <Button value="4" columnIndex={index} />
          </div>
          <div className={style.numberCell}>
            <Button value="5" columnIndex={index} />
          </div>
          <div className={style.numberCell}>
            <Button value="6" columnIndex={index} />
          </div>
          <div className={style.numberCell}>
            <Button value="7" columnIndex={index} />
          </div>
          <div className={style.numberCell}>
            <Button value="8" columnIndex={index} />
          </div>
          <div className={style.numberCell}>
            <Button value="9" columnIndex={index} />
          </div>
        </div>
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

const Button = props => (
  <div
    className={style.button}
    onClick={() => props.onClick(props.columnIndex, props.value)}>
    <div className={style.buttonLabel}>{props.value}</div>
  </div>
)

export default FreeResponseAnswerer
