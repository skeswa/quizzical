
import pluralize from 'pluralize'
import { createAction } from 'redux-actions'

export function createCrudActions(entity, crudService, extender) {
  const bumpPendingRequests = createAction(`bump pending ${entity} requests`)
  const preAction = () => ({ preAction: bumpPendingRequests() })

  let extensions = null
  if (extender) {
    extensions = extender(preAction)
  }

  return Object.assign({
    [`load${camelizeEntity(entity)}`]: createAction(`load ${entity}`, crudService.get, preAction),
    [`create${camelizeEntity(entity)}`]: createAction(`create ${entity}`, crudService.create, preAction),
    [`delete${camelizeEntity(entity)}`]: createAction(`delete ${entity}`, crudService.del, preAction),
    [`load${camelizeEntity(pluralizeEntity(entity))}`]: createAction(`load ${pluralizeEntity(entity)}`, crudService.getAll, preAction),
  }, extensions)
}

const capitalize = string => string.charAt(0).toUpperCase() + string.slice(1)

function camelizeEntity(entity) {
  if (entity.indexOf(' ') !== -1) {
    return entity.split(' ').map(part => capitalize(part)).join('')
  }

  return capitalize(entity)
}

function pluralizeEntity(entity) {
  const slashIndex = entity.lastIndexOf(' ')
  if (slashIndex !== -1) {
    const entityPath = entity.substring(0, slashIndex)
    const entityName = entity.substring(slashIndex + 1)

    return `${entityPath} ${pluralize(entityName)}`
  }

  return pluralize(entity)
}
