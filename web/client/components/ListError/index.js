
import React, { Component } from 'react'
import style from './style.css'

const ListError = (props, context) => (
  <div className={style.main}>
    <div className={style.error}>
      <div className={style.icon}>
        <i className="material-icons">error</i>
      </div>
      <div className={style.text}>
        <div className={style.title}>List failed to load.</div>
        <div className={style.message}>{props.error}</div>
      </div>
    </div>
  </div>
)

export default ListError
