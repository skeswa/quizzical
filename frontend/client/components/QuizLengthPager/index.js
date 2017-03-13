
import FontIcon from 'material-ui/FontIcon';
import classNames from 'classnames'
import IconButton from 'material-ui/IconButton';
import React, { Component, PropTypes } from 'react'

import style from './index.css'

const ARROW_BUTTON_ICON_STYLE = { fontSize: '2rem' }

class QuizLengthPager extends Component {
  static propTypes = {
    style:                      PropTypes.object,
    lengths:                    PropTypes.array.isRequired,
    selectedLengthIndex:        PropTypes.number.isRequired,
    onSelectedLengthChanged:    PropTypes.func.isRequired,
  }

  onPrev() {
    const { selectedLengthIndex, onSelectedLengthChanged } = this.props

    if (selectedLengthIndex > 0) {
      onSelectedLengthChanged(selectedLengthIndex - 1)
    }
  }

  onNext() {
    const { lengths, selectedLengthIndex, onSelectedLengthChanged } = this.props

    if (selectedLengthIndex < lengths.length - 1) {
      onSelectedLengthChanged(selectedLengthIndex + 1)
    }
  }

  render() {
    const {
      style: _style,

      lengths,
      selectedLengthIndex,
      onSelectedLengthChanged,
    } = this.props

    return (
      <div style={_style} className={style.main}>
        <div className={style.arrow}>
          <IconButton
            onClick={this.onPrev.bind(this)}
            disabled={selectedLengthIndex <= 0}
            iconStyle={ARROW_BUTTON_ICON_STYLE}>
            <FontIcon className="material-icons">arrow_back</FontIcon>
          </IconButton>
        </div>
        <div className={style.details}>
          <div className={style.length}>{lengths[selectedLengthIndex]}</div>
          <div className={style.label}>Quiz Length</div>
        </div>
        <div className={style.arrow}>
          <IconButton
            onClick={this.onNext.bind(this)}
            disabled={selectedLengthIndex >= lengths.length - 1}
            iconStyle={ARROW_BUTTON_ICON_STYLE}>
            <FontIcon className="material-icons">arrow_forward</FontIcon>
          </IconButton>
        </div>
      </div>
    )
  }
}

export default QuizLengthPager
