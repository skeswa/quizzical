
import FloatingActionButton from 'material-ui/FloatingActionButton'
import React, { Component } from 'react'
import classNames from 'classnames'
import FontIcon from 'material-ui/FontIcon'

import style from './style.css'

const ListButtons = (props, context) => (
  <div className={style.main}>
    <FloatingActionButton
      mini={true}
      onClick={props.switchToGrid ? props.onSwitchToGridClicked : props.onSwitchToTableClicked}
      className={style.button}>
      <FontIcon className="material-icons">{props.switchToGrid ? 'view_module' : 'view_list'}</FontIcon>
    </FloatingActionButton>
    <FloatingActionButton
      mini={true}
      onClick={props.onRefreshClicked}
      className={style.button}>
      <FontIcon className="material-icons">refresh</FontIcon>
    </FloatingActionButton>
    <FloatingActionButton
      onClick={props.onCreateClicked}
      disabled={props.disabled}
      className={style.button}>
      <FontIcon className="material-icons">add</FontIcon>
    </FloatingActionButton>
  </div>
)

export default ListButtons
