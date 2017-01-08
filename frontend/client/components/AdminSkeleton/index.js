
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import Header from 'components/Header'
import style from './style.css'
import Nav from 'components/Nav'

const AdminSkeleton = props => (
  <div className={style.main}>
    <div className={style.top}>
      <Header />
    </div>
    <div className={style.bottom}>
      <Nav />
      {props.children}
    </div>
  </div>
)

export default AdminSkeleton
