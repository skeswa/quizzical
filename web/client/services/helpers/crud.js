
import debug from 'debug'
import pluralize from 'pluralize'

const logger = debug('gauntlet:crud:service')
const genericFailureError = 'Sorry. For whatever reason, the server didn\'t like that.';
const connectionFailureError = 'Could not reach the server. Make sure you have a good connection.';

export function crudService(entity, extensions) {
  const endpoint = `/api/${pluralize(entity)}`

  return Object.assign({
    get(id) {
      return fetch(`${endpoint}/${id}`, { method: 'GET' })
        .then(handleSuccess, handleFailure)
    },

    getAll() {
      return fetch(endpoint, { method: 'GET' })
        .then(handleSuccess, handleFailure)
    },

    create(payload) {
      const body = payload instanceof FormData
        ? payload
        : JSON.stringify(payload)

      return fetch(endpoint, { method: 'POST', body })
        .then(handleSuccess, handleFailure)
        .then(creationRecord => {
          return fetch(`${endpoint}/${creationRecord.createdRecordId}`)
            .then(handleSuccess, handleFailure)
        })
    },

    del(id) {
      return fetch(`${endpoint}/${id}`, { method: 'POST' })
        .then(handleSuccess, handleFailure)
    },
  }, extensions)
}

function handleSuccess(response) {
  return response.ok
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
