
import { handleActions } from 'redux-actions'

import { normalizeEntity, pluralizeNormalizedEntity } from 'utils/crud'

const baseInitialState = {
  map:              [],
  list:             [],
  loaded:           false,
  pendingRequests:  0,
}

export function createCrudReducer(entity, actionHandlerExtensions, initialStateExtensions) {
  const initialState = Object.assign(
    {},
    baseInitialState,
    initialStateExtensions)

  const normalizedEntity = normalizeEntity(entity)
  const pluralNormalizedEntity = pluralizeNormalizedEntity(normalizedEntity)

  const actionHandlers = Object.assign({
    [`load ${normalizedEntity}`]: handleLoadOneAction,
    [`load ${pluralNormalizedEntity}`]: handleLoadAllAction,
    [`create ${normalizedEntity}`]: handleCreateAction,
    [`delete ${normalizedEntity}`]: handleDeleteAction,
    [`bump pending ${normalizedEntity} requests`]: handleBumpPendingRequestsAction,
  }, actionHandlerExtensions)

  return handleActions(actionHandlers, initialState)
}

function handleLoadOneAction(state, action) {
  if (action.error) {
    return Object.assign({}, state, {
      pendingRequests: decrementedPendingRequests(state),
    })
  }

  return Object.assign({}, state, {
    map: action.payload && action.payload.id
      ? Object.assign({}, state.map, { [action.payload.id]: action.payload })
      : state.map,
    list: appendOrUpdate(state.list, action.payload),
    pendingRequests: decrementedPendingRequests(state),
  })
}

function handleLoadAllAction(state, action) {
  if (action.error) {
    return Object.assign({}, state, {
      pendingRequests: decrementedPendingRequests(state),
    })
  }

  return Object.assign({}, state, {
    map: (action.payload || [])
      .filter(item => item && item.id)
      .reduce((map, item) => {
        map[item.id] = item
        return map
      }),
    list: action.payload || [],
    loaded: true,
    pendingRequests: decrementedPendingRequests(state),
  })
}

function handleCreateAction(state, action) {
  if (action.error) {
    return Object.assign({}, state, {
      pendingRequests: decrementedPendingRequests(state),
    })
  }

  return Object.assign({}, state, {
    map: action.payload && action.payload.id
      ? Object.assign({}, state.map, { [action.payload.id]: action.payload })
      : state.map,
    list: appendOrUpdate(state.list, action.payload),
    pendingRequests: decrementedPendingRequests(state),
  })
}

function handleDeleteAction(state, action) {
  if (action.error) {
    return Object.assign({}, state, {
      error: action.payload,
      pendingRequests: decrementedPendingRequests(state),
    })
  }

  return Object.assign({}, state, {
    map: filterKey(state.map, action.payload.deletedRecordId),
    list: state.list.filter(item => item.id !== action.payload.deletedRecordId),
    pendingRequests: decrementedPendingRequests(state),
  })
}

function handleBumpPendingRequestsAction(state, action) {
  return Object.assign({}, state, {
    pendingRequests: incrementedPendingRequests(state),
  })
}

function decrementedPendingRequests(state) {
  return state.pendingRequests > 0 ? (state.pendingRequests - 1) : 0
}

function incrementedPendingRequests(state) {
  return state.pendingRequests < 0 ? 1 : (state.pendingRequests + 1)
}

function appendOrUpdate(items, newItem) {
  let updated = false
  const newItems = items.map(item => {
    if (!updated && item.id === newItem.id) {
      updated = true
      return newItem
    }

    return item
  })

  if (!updated) {
    newItems.push(newItem)
  }

  return newItems
}

function filterKey(map, key) {
  if (!map) return null
  if (!map.hasOwnProperty(key)) return map

  const newMap = Object.assign({}, map)
  delete newMap[key]
  return newMap
}
