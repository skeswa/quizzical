
import { createAction } from 'redux-actions'
import { get, getAll, create, delete } from 'services/questions'

export const loadQuestion = createAction('load question', get)
export const loadQuestions = createAction('load questions', getAll)
export const createQuestion = createAction('create question', payload =>
  create(payload).then(response => get(response.newRecordId)))
export const deleteQuestion = createAction('delete question', delete)
