
import classNames from 'classnames'

import React, { Component } from 'react'
import style from './style.css'

export default class Header extends Component {
  render() {
    return (
      <header className={style.main}>
        <div className={style.middle}>
          <div className={style.logo}>Gauntlet</div>
          <div className={style.account}>
            <i className={classNames(style.accountIcon, 'material-icons')}>account_circle</i>
          </div>
        </div>
      </header>
    )
  }
}
