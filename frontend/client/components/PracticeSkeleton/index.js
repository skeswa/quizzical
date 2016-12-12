
import React, { Component } from 'react'

import style from './style.css'

const PracticeSkeleton = props => (
  <div className={style.main}>
    <div className={style.header}>Quizzical</div>
    <div className={style.body}>{props.children}</div>
  </div>
)

export default PracticeSkeleton
