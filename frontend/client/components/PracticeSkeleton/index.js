
import React, { Component } from 'react'

import style from './style.css'

const PracticeSkeleton = props => (
  <div className={style.main}>
    <div className={style.header}>
      <div className={style.title}>{props.title}</div>
      {
        props.subtitle
        ? <div className={style.subtitle}>{props.subtitle}</div>
        : null
      }
    </div>
    <div className={style.body}>{props.children}</div>
  </div>
)

export default PracticeSkeleton
