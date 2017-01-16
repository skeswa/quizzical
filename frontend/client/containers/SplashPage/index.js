
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component } from 'react'

import style from './style.css'
import logoURL from 'resources/images/splash-logo.png'

const LABEL_STYLE       = { fontSize: '1.8rem' }
const BUTTON_STYLE      = { height: '5rem' }
const TOP_BUTTON_STYLE  = { marginBottom: '1rem' }

class SplashPage extends Component {
  static contextTypes = {
    router: React.PropTypes.object.isRequired,
  }

  onSignInClicked() {
    this.context.router.push(`/quiz/start`)
  }

  render() {
    return (
      <div className={style.main}>
        <img className={style.logo} src={logoURL} />
        <div className={style.buttons}>
          <RaisedButton
            style={TOP_BUTTON_STYLE}
            label="Learn More"
            labelColor="#754aec"
            labelStyle={LABEL_STYLE}
            buttonStyle={BUTTON_STYLE}
            backgroundColor="#ffffff" />
          <RaisedButton
            label="Sign In"
            onClick={::this.onSignInClicked}
            labelColor="#ffffff"
            labelStyle={LABEL_STYLE}
            buttonStyle={BUTTON_STYLE}
            backgroundColor="#222222" />
        </div>
      </div>
    )
  }
}

export default SplashPage
