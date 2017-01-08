
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component } from 'react'

import style from './style.css'
import PracticeSkeleton from 'components/PracticeSkeleton'

const NotFoundPage = props => (
  <PracticeSkeleton title="Quizzical" subtitle="Page not found">
    <div className={style.main}>
      <div className={style.messageWrapper}>
        <div className={style.message}>
          <i className={`material-icons ${style.icon}`}>report</i>
          <div className={style.text}>
            Sorry, we couldn't find the page you were looking for.
          </div>
        </div>
      </div>
      <div className={style.buttonWrapper}>
        <RaisedButton
          href="/quiz/start"
          label="Start a Quiz"
          labelColor="#ffffff"
          backgroundColor="#222222" />
      </div>
    </div>
  </PracticeSkeleton>
)

export default NotFoundPage
