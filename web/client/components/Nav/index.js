
import { Link } from 'react-router'
import classNames from 'classnames'
import React, { Component } from 'react'

import style from './style.css'

const NavItem = (props, context) => {
  const { title, path, icon } = props

  return (
    <Link
      to={path}
      className={style.item}
      activeClassName={style.item__highlighted}>
      <i className={classNames(style.itemIcon, 'material-icons')}>{icon}</i>
      <div className={style.itemTitle}>{title}</div>
    </Link>
  )
}

const Nav = (props, context) => (
  <div className={style.main}>
    <NavItem
      icon="question_answer"
      title="Questions"
      path="/questions" />
    <NavItem
      icon="mode_edit"
      title="Practice"
      path="/practice" />
    <NavItem
      icon="chrome_reader_mode"
      title="Quizzes"
      path="/quizzes" />
  </div>
)

export default Nav
