
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import Header from '../../components/Header'
import Footer from '../../components/Footer'
import style from './style.css'

class App extends Component {
  render() {
    return (
      <div className={style.main}>
        <div className={style.top}>
          <Header />
        </div>
        <div className={style.middle}>
          {this.props.children}
        </div>
        <div className={style.bottom}>
          <Footer />
        </div>
      </div>
    )
  }
}

export default App
