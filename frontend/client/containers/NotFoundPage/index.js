
import RaisedButton from 'material-ui/RaisedButton'
import { withRouter } from 'react-router'
import React, { Component } from 'react'

import style from './style.css'
import PracticeSkeleton from 'components/PracticeSkeleton'

const NotFoundPage = ({ history }) => (
  <PracticeSkeleton
    title="Page not found"
    action="Take a Quiz"
    animated={true}
    animationDelay={100}
    onActionClicked={() => history.push('/quiz')}>
    <div className={style.main}>
      <i className={`material-icons ${style.icon}`}>report</i>
      <div className={style.text}>
        <div>Sorry, we couldn't find the</div>
        <div>page you were looking for.</div>
      </div>
    </div>
  </PracticeSkeleton>
)

// Connect the not found page to the router so that it can perform history
// operations.
const NotFoundPageWithRouter = withRouter(NotFoundPage)

export default NotFoundPageWithRouter
