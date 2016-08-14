
import FloatingActionButton from 'material-ui/FloatingActionButton'
import React, { Component } from 'react'
import classNames from 'classnames'
import FontIcon from 'material-ui/FontIcon'

import style from './style.css'

const ListButtons = (props, context) => (
  <div className={style.main}>
    <FloatingActionButton
      mini={true}
      onClick={props.onRefreshClicked}
      disabled={props.disabled}>
      <FontIcon className="material-icons">refresh</FontIcon>
    </FloatingActionButton>
    <FloatingActionButton
      className={style.bottomButton}
      onClick={props.onCreateClicked}
      disabled={props.disabled}>
      <FontIcon className="material-icons">add</FontIcon>
    </FloatingActionButton>
  </div>
)

export default ListButtons
