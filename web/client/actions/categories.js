
import { createAction } from 'redux-actions'
import { get, getAll, create, delete } from 'services/categories'

export const loadCategory = createAction('load category', get)
export const loadCategories = createAction('load categories', getAll)
export const createCategory = createAction('create category', payload =>
  create(payload).then(response => get(response.newRecordId)))
export const deleteCategory = createAction('delete category', delete)
