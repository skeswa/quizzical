
import { createAction } from 'redux-actions'

import {
  normalizeEntity,
  camelizeNormalizedEntity,
  pluralizeNormalizedEntity,
} from 'utils/crud'

export function createCrudActions(entity, crudService, extender) {
  const normalizedEntity = normalizeEntity(entity)
  const bumpPendingRequests = createAction(`bump pending ${normalizedEntity} requests`)
  const preAction = () => ({ preAction: bumpPendingRequests() })

  let extensions = null
  if (extender) {
    extensions = extender(preAction)
  }

  const camelCasedEntity = camelizeNormalizedEntity(normalizedEntity)
  const pluralNormalizedEntity = pluralizeNormalizedEntity(normalizedEntity)
  const pluralCamelCasedEntity = camelizeNormalizedEntity(pluralNormalizedEntity)

  return Object.assign({
    [`load${camelCasedEntity}`]: createAction(`load ${normalizedEntity}`, crudService.get, preAction),
    [`create${camelCasedEntity}`]: createAction(`create ${normalizedEntity}`, crudService.create, preAction),
    [`delete${camelCasedEntity}`]: createAction(`delete ${normalizedEntity}`, crudService.del, preAction),
    [`load${pluralCamelCasedEntity}`]: createAction(`load ${pluralNormalizedEntity}`, crudService.getAll, preAction),
  }, extensions)
}
