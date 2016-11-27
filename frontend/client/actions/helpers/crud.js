
import pluralize from 'pluralize'
import { createAction } from 'redux-actions'

const capitalize = string => string.charAt(0).toUpperCase() + string.slice(1)

export function createCrudActions(entity, crudService, extensions) {
  const bumpPendingRequests = createAction(`bump pending ${entity} requests`)
  const preAction = () => ({ preAction: bumpPendingRequests() })

  return Object.assign({
    [`load${capitalize(entity)}`]: createAction(`load ${entity}`, crudService.get, preAction),
    [`create${capitalize(entity)}`]: createAction(`create ${entity}`, crudService.create, preAction),
    [`delete${capitalize(entity)}`]: createAction(`delete ${entity}`, crudService.del, preAction),
    [`load${capitalize(pluralize(entity))}`]: createAction(`load ${pluralize(entity)}`, crudService.getAll, preAction),
  }, extensions)
}
