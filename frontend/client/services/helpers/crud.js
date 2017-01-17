
import debug from 'debug'
import Network from 'utils/network'

import { endpointSuffixFor } from 'utils/crud'

const logger = debug('gauntlet:crud:service')
const genericFailureError = 'Sorry. For whatever reason, the server isn\'t co-operating. Let\'s try that again.'
const connectionFailureError = 'Could not reach the server. Make sure you have a good connection.'

export function crudService(entity, extender) {
  const endpoint = `/api/${endpointSuffixFor(entity)}`

  return Object.assign({
    get(id) {
      return Network.get(`${endpoint}/${id}`)
    },

    getAll(offset = 0, limit = 500) {
      return Network.get(`${endpoint}?start=${offset}&end=${offset + limit}`)
    },

    create(payload) {
      return Network.put(endpoint, body)
    },

    del(id) {
      return Network.delete(`${endpoint}/${id}`)
    },
  }, extender ? extender(endpoint) : null)
}

export function handleSuccess(response) {
  return response.ok
    ? deserializeResponse(response)
    : Promise.reject(deserializeError(response))
}

export function handleFailure(problem) {
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
