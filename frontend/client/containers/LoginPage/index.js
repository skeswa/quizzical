
import TextField from 'material-ui/TextField'
import { connect } from 'react-redux'
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import RaisedButton from 'material-ui/RaisedButton'
import RefreshIndicator from 'material-ui/RefreshIndicator'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import style from './style.css'
import actions from 'actions'
import FormError from 'components/FormError'
import PracticeSkeleton from 'components/PracticeSkeleton'
import { extractErrorFromResultingActions } from 'utils'

const DARK_MUI_THEME = getMuiTheme({
  textField: {
    floatingLabelColor: '#ffffff',
    focusColor: '#ffffff',
    textColor: '#ffffff',
  },
});

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

  onPasswordKeyUp(e) {
    if (e.keyCode === 13) {
      this.onLoginClicked()
    }
  }

  onEmailChanged(e, email) {
    this.setState({ email })
  }

  onPasswordChanged(e, password) {
    this.setState({ password })
  }

  onLoginClicked() {
    const { router } = this.context
    const { email, password } = this.state
    const { actions, location } = this.props

    this.setState({ loading: true })
    actions.login(email, password).then(resultingActions => {
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

  renderError() {
    const { error } = this.state

    if (error) {
      const message = error.error
        ? error.error
        : error

      return (
        <FormError
          title="The credentials you used are incorrect."
          message={message}
          limitHeight={true} />
      )
    }

    return null
  }

  render() {
    return (
      <PracticeSkeleton title="Quizzical" subtitle="Sign In Below">
        <MuiThemeProvider muiTheme={DARK_MUI_THEME}>
          <div className={style.main}>
            <div className={style.middle}>
              {this.renderError()}

              <TextField
                onChange={::this.onEmailChanged}
                disabled={this.state.loading}
                fullWidth={true}
                floatingLabelText="Email" />
              <TextField
                type="password"
                onKeyUp={::this.onPasswordKeyUp}
                onChange={::this.onPasswordChanged}
                disabled={this.state.loading}
                fullWidth={true}
                floatingLabelText="Password" />
            </div>
            <div className={style.bottom}>
              <RaisedButton
                label="Sign In"
                onClick={::this.onLoginClicked}
                disabled={this.state.loading}
                labelColor="#754aec"
                backgroundColor="#ffffff" />
            </div>
          </div>
        </MuiThemeProvider>
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
