
import classNames from 'classnames'
import RaisedButton from 'material-ui/RaisedButton'
import { withRouter } from 'react-router'
import React, { Component, PropTypes } from 'react'

import style from './style.css'
import logoURL from 'resources/images/q.png'
import splashVideoURL from 'resources/videos/student-taking-test.mp4'

class SplashPage extends Component {
  static propTypes = {
    history: PropTypes.object.isRequired,
  }

  state = {
    visible: false,
  }

  onSignInClicked() {
    this.setState(
      { visible: false },
      () => setTimeout(() => this.props.history.push('/quiz'), 300))
  }

  componentDidMount() {
    setTimeout(() => this.setState({ visible: true }), 200)
  }

  render() {
    const { visible } = this.state

    const mainClassName = classNames(
      style.main, { [style.main__visible]: visible })

    return (
      <div className={mainClassName}>
        <div className={style.back}>
          <video className={style.video} src={splashVideoURL} loop autoPlay />
        </div>
        <div className={style.front}>
          <div className={style.left}>
            <img className={style.logo} src={logoURL} />
            <div className={style.blurb}>
              <span className={style.bold}>Quizzical</span> helps you get better
              at the math section of the SAT.
            </div>
            <RaisedButton
              style={{ width: '14rem' }}
              label="Continue"
              onClick={::this.onSignInClicked}
              labelColor="#754aec"
              labelStyle={{ fontSize: '1.8rem' }}
              backgroundColor="#ffffff" />
          </div>
        </div>
      </div>
    )
  }
}

// Connect the splash page to the router so that it can perform history
// operations.
const SplashPageWithRouter = withRouter(SplashPage)

export default SplashPageWithRouter
