
import { createAction } from 'redux-actions'
import { get, getAll, create, delete } from 'services/quizzes'

export const loadQuiz = createAction('load quiz', get)
export const loadQuizzes = createAction('load quizzes', getAll)
export const createQuiz = createAction('create quiz', payload =>
  create(payload).then(response => get(response.newRecordId)))
export const deleteQuiz = createAction('delete quiz', delete)
