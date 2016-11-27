
import React, { Component } from 'react'

import style from './style.css'
import { Cell } from 'components/Table'

const SortTypes = {
  ASC:  'asc',
  DESC: 'desc',
  NONE: 'none',
}

class TableHeaderCell extends Component {
  static propTypes = {
    sort: React.PropTypes.oneOf([
      SortTypes.ASC,
      SortTypes.DESC,
      SortTypes.NONE,
    ]),

    title:        React.PropTypes.string.isRequired,
    onSortChange: React.PropTypes.func,
  }

  renderSortIndicator() {
    // TODO(skeswa): implement the sort indicator.
    return null
  }

  render() {
    return (
      <Cell {...this.props}>
        <div className={style.main}>
          <div className={style.title}>{this.props.title}</div>
          {this.renderSortIndicator()}
        </div>
      </Cell>
    )
  }
}

export { SortTypes, TableHeaderCell as default }
