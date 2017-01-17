
import Network from 'utils/network'
import Session from 'utils/session'


const ERROR_MESSAGE             = 'Failed to authenticate with the ' +
  'credentials provided. We implore you to try again - but with the correct ' +
  'credentials this time.'
const AUTHORIZATION_HEADER      = 'Authorization'
const authorizationHeaderRegex  = /^Basic\s+([^\s]+)/

const AuthService = {
  login(email, password) {
    return Network.post(
      '/api/auth/login',
      { username: email, password },
      true)
      .then(response => {
        if (response.ok && response.status < 400) {
          const header = response.headers.get(AUTHORIZATION_HEADER)
          const token = authorizationHeaderRegex.exec(header)[1]

          return response.json().then(sessionUser => {
            Session.create(token, sessionUser)
            return Promise.resolve(sessionUser)
          })
        }

        return Promise.reject(ERROR_MESSAGE)
      })
  }
}

export default AuthService
