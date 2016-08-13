
import pluralize from 'pluralize'
import { createAction } from 'redux-actions'

const capitalize = string => string.charAt(0).toUpperCase() + string.slice(1)

export function createCrudActions(entity, crudService, extensions) {
  return Object.assign({
    [`load${capitalize(entity)}`]: createAction(`load ${entity}`, crudService.get),
    [`create${capitalize(entity)}`]: createAction(`create ${entity}`, crudService.create),
    [`delete${capitalize(entity)}`]: createAction(`delete ${entity}`, crudService.del),
    [`load${capitalize(pluralize(entity))}`]: createAction(`load ${pluralize(entity)}`, crudService.getAll),
  }, extensions)
}
