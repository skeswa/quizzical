
import React, { Component } from 'react'
import style from './style.css'

const FormError = (props, context) => (
  <div className={style.main}>
    <div className={style.error}>
      <div className={style.icon}>
        <i className="material-icons">error</i>
      </div>
      <div className={style.text}>
        <div className={style.title}>{props.title}</div>
        <div className={
          style.message + (
            props.limitHeight
            ? style.message__limitedHeight
            : '')
        }>{props.message}</div>
      </div>
    </div>
  </div>
)

export default FormError
