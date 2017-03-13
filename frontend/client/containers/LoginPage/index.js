
import TextField from 'material-ui/TextField'
import { connect } from 'react-redux'
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import RaisedButton from 'material-ui/RaisedButton'
import RefreshIndicator from 'material-ui/RefreshIndicator'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import style from './index.css'
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
        <div className={style.formErrorWrapper}>
          <FormError
            title="The credentials you used are incorrect."
            message={message}
            limitHeight={true} />
        </div>
      )
    }

    return null
  }

  render() {
    return (
      <PracticeSkeleton
        title="Sign In"
        action="Sign In"
        animated={true}
        actionDisabled={this.state.loading}
        animationDelay={100}
        onActionClicked={::this.onLoginClicked}>
        {this.renderError()}

        <TextField
          onChange={::this.onEmailChanged}
          disabled={this.state.loading}
          fullWidth={true}
          floatingLabelText="Email"
          floatingLabelFixed={true} />
        <TextField
          type="password"
          onKeyUp={::this.onPasswordKeyUp}
          onChange={::this.onPasswordChanged}
          disabled={this.state.loading}
          fullWidth={true}
          floatingLabelText="Password"
          floatingLabelFixed={true} />

        <div className={style.forgotPass}>
          <span>Click here if you </span>
          <a className={style.forgotPassLink} href="#">forgot your password or email</a>
          <span>.</span>
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
