
import { handleSuccess, handleFailure } from './helpers/crud'

const AuthService = {
  login(email, password) {
    return fetch('/api/login', { method: 'POST' })
      .then(handleSuccess, handleFailure)
  },

  whoami() {
    return fetch('/api/whoami', { method: 'GET' })
      .then(handleSuccess, handleFailure)
  },
}

export default AuthService
