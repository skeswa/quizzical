
import TextField from 'material-ui/TextField'
import { connect } from 'react-redux'
import RaisedButton from 'material-ui/RaisedButton'
import RefreshIndicator from 'material-ui/RefreshIndicator'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import style from './style.css'
import actions from 'actions'
import PracticeSkeleton from 'components/PracticeSkeleton'
import { extractErrorFromResultingActions } from 'utils'

class LoginPage extends Component {
  static contextTypes = {
    router: React.PropTypes.object.isRequired,
  }

  state = {
    error: null,
    email: null,
    loading: false,
    password: null,
  }

  onEmailChanged(e, email) {
    this.setState({ email })
  }

  onPasswordChanged(e, password) {
    this.setState({ password })
  }

  onLoginClicked() {
    const { email, password } = this.state
    const { login, router, location } = this.state

    this.setState({ loading: true })
    login(email, password).then(resultingActions => {
      const error = extractErrorFromResultingActions(resultingActions)
      if (error) {
        this.setState({ error, loading: false })
        return
      }

      if (location.state && location.state.nextPathname) {
          router.replace(location.state.nextPathname)
        } else {
          router.replace('/quiz/start')
        }
    })
  }

  render() {
    return (
      <PracticeSkeleton title="Quizzical" subtitle="Sign In Below">
        <div style={style.main}>
          <TextField
            onChange={::this.onEmailChanged}
            disabled={this.state.loading}
            floatingLabelText="Email" />
          <TextField
            onChange={::this.onPasswordChanged}
            disabled={this.state.loading}
            floatingLabelText="Password" />
          <RaisedButton
            label="Sign In"
            onClick={::this.onLoginClicked}
            disabled={this.state.loading}
            labelColor="#754aec"
            backgroundColor="#ffffff" />
        </div>
      </PracticeSkeleton>
    )
  }
}

const reduxify = connect(
  (state, props) => ({
    authed: state.auth.authed,
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.auth, dispatch)),
  }))

export default reduxify(LoginPage)
