
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import Header from 'components/Header'
import style from './style.css'
import Nav from 'components/Nav'

class App extends Component {
  render() {
    return (
      <div className={style.main}>
        <div className={style.top}>
          <Header />
        </div>
        <div className={style.bottom}>
          <Nav />
          <div className={style.content}>
            {this.props.children}
          </div>
        </div>
      </div>
    )
  }
}

export default App
