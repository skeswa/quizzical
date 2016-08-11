
import { createAction } from 'redux-actions'
import { get, getAll, create, delete } from 'services/takes'

export const loadTake = createAction('load take', getAll)
export const loadTakes = createAction('load takes', getAll)
export const createTake = createAction('create take', payload =>
  create(payload).then(response => get(response.newRecordId)))
export const deleteTake = createAction('delete take', delete)
