
import React, { Component } from 'react'
import style from './style.css'

const ListError = (props, context) => (
  <div className={style.main}>
    <div className={style.message}>There are no records to show.</div>
  </div>
)

export default ListError
