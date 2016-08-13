
import React, { Component } from 'react'
import classNames from 'classnames'

import style from './style.css'

const NavItem = (props, context) => {
  const { title, icon, showIcon, highlighted } = props

  return (
    <div className={classNames(style.item, { [style.item__highlighted]: highlighted })}>
      <i className={classNames(style.itemIcon, 'material-icons')}>{icon}</i>
      <div className={style.itemTitle}>{title}</div>
    </div>
  )
}

const Nav = (props, context) => (
  <div className={style.main}>
    <NavItem icon="question_answer" title="Questions" showIcon={props.showIcon} />
    <NavItem icon="mode_edit" title="Practice" highlighted={true} />
    <NavItem icon="chrome_reader_mode" title="Quizzes" showIcon={props.showIcon} />
  </div>
)

export default Nav
