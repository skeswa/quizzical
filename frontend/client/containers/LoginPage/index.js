
import autobind from 'autobind-decorator'
import TextField from 'material-ui/TextField'
import { connect } from 'react-redux'
import RaisedButton from 'material-ui/RaisedButton'
import { withRouter } from 'react-router'
import RefreshIndicator from 'material-ui/RefreshIndicator'
import { bindActionCreators } from 'redux'
import React, { Component, PropTypes } from 'react'

import style from './index.css'
import actions from 'actions'
import FormError from 'components/FormError'
import PracticeSkeleton from 'components/PracticeSkeleton'
import { extractErrorFromResultingActions } from 'utils'

class LoginPage extends Component {
  static propTypes = {
    history: PropTypes.object.isRequired,
    location: PropTypes.object.isRequired,
  }

  state = {
    error:    null,
    email:    null,
    loading:  false,
    password: null,
  }

  @autobind
  onPasswordKeyUp(e) {
    if (e.keyCode === 13) {
      this.onLoginClicked()
    }
  }

  @autobind
  onEmailChanged(e, email) {
    this.setState({ email })
  }

  @autobind
  onPasswordChanged(e, password) {
    this.setState({ password })
  }

  @autobind
  onLoginClicked() {
    const { email, password } = this.state
    const { actions, location, history } = this.props

    this.setState({ loading: true })
    actions.login(email, password)
      .then(resultingActions => {
        const error = extractErrorFromResultingActions(resultingActions)
        if (error) {
          this.setState({ error, loading: false })
          return
        }

        if (location.state && location.state.from) {
          history.replace(location.state.from)
        } else {
          history.replace('/quiz')
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
        onActionClicked={this.onLoginClicked}>
        <div className={style.main}>
          {this.renderError()}

          <TextField
            onChange={this.onEmailChanged}
            disabled={this.state.loading}
            fullWidth={true}
            floatingLabelText="Email"
            floatingLabelFixed={true} />
          <TextField
            type="password"
            onKeyUp={this.onPasswordKeyUp}
            onChange={this.onPasswordChanged}
            disabled={this.state.loading}
            fullWidth={true}
            floatingLabelText="Password"
            floatingLabelFixed={true} />

          <div className={style.forgotPass}>
            <span>Click here if you </span>
            <a className={style.forgotPassLink} href="#">
              forgot your password or email
            </a>
            <span>.</span>
          </div>
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

// Connect the login page to the store.
const LoginPageWithRedux = reduxify(LoginPage)
// Connect the login page to the router so that it can perform history
// operations.
const LoginPageWithReduxWithRouter = withRouter(LoginPageWithRedux)

export default LoginPageWithReduxWithRouter
