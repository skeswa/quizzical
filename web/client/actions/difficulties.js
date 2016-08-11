
import { createAction } from 'redux-actions'
import { get, getAll, create, delete } from 'services/difficulties'

export const loadDifficulty = createAction('load difficulty', get)
export const loadDifficulties = createAction('load difficulties', getAll)
export const createDifficulty = createAction('create difficulty', payload =>
  create(payload).then(response => get(response.newRecordId)))
export const deleteDifficulty = createAction('delete difficulty', delete)
