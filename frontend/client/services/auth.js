
import Network from 'utils/network'
import Session from 'utils/session'

const AuthService = {
  login(email, password) {
    return Network.post(
      '/api/auth/login',
      { username: email, password }).then(response => {
        debugger
        return Promise.resolve(response)
      })
  }
}

export default AuthService
