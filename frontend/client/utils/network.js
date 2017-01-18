
import debug from 'debug'
import Session from 'utils/session'

const METHOD_GET        = 'GET'
const METHOD_PUT        = 'PUT'
const METHOD_POST       = 'POST'
const METHOD_DELETE     = 'DELETE'
const CONTENT_TYPE_JSON = 'application/json'

const logger = debug('gauntlet:network')
const genericFailureError = 'Sorry. For whatever reason, the server isn\'t co-operating. Let\'s try that again.'
const connectionFailureError = 'Could not reach the server. Make sure you have a good connection.'

function getToken() {
  const session = Session.retrieve()
  if (!session) return null

  return session.token
}

function request(method, url, body, returnResponse, headers) {
  const token = getToken()
  const modifiedHeaders = Object.assign(
    { 'Accept': CONTENT_TYPE_JSON },
    token ? { 'Authorization': `Bearer ${token}` } : null,
    body && !(body instanceof FormData) ? {
      'Content-Type': CONTENT_TYPE_JSON,
    } : null,
    headers)
  const serializedBody = (body instanceof FormData)
      ? body
      : JSON.stringify(body)

  const response = fetch(
    url,
    { method, headers: modifiedHeaders, body: serializedBody })

  return returnResponse
    ? response
    : response.then(handleSuccess, handleFailure)
}

function handleSuccess(response) {
  return (response.ok && response.status < 400)
    ? deserializeResponse(response)
    : Promise.reject(deserializeError(response))
}

function handleFailure(problem) {
  logger('Service request failed:', problem)
  return Promise.reject(connectionFailureError)
}

function deserializeResponse(response) {
  if (response.headers.get('Content-Type') === 'application/json') {
    return response.json()
  }

  return response.text()
}

function deserializeError(response) {
  return deserializeResponse(response)
    .then(data => data ? data : genericFailureError)
}

const Network = {
  get:    request.bind(null, METHOD_GET),
  put:    request.bind(null, METHOD_PUT),
  post:   request.bind(null, METHOD_POST),
  delete: request.bind(null, METHOD_DELETE),
}

export default Network
