
import { handleSuccess, handleFailure } from './helpers/crud'

const JSON_HEADERS = {
  'Accept': 'application/json',
  'Content-Type': 'application/json',
}

const AuthService = {
  login(email, password) {
    const body = JSON.stringify({ username: email, password }),
      headers = JSON_HEADERS
    return fetch('/api/auth/login', { method: 'POST', headers, body })
      .then(handleSuccess, handleFailure)
  },

  whoami() {
    return fetch('/api/auth/whoami', { method: 'GET' })
      .then(handleSuccess, handleFailure)
  },
}

export default AuthService
