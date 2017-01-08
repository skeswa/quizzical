
import { crudService } from './helpers/crud'

export default crudService(
  'quiz',
  (endpoint, handleSuccess, handleFailure) => ({
    generate(payload) {
      return fetch(`${endpoint}/generate`, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload),
      }).then(handleSuccess, handleFailure)
    }
  }))
