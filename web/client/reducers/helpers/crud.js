
import pluralize from 'pluralize'
import { handleActions } from 'redux-actions'

const baseInitialState = {
  list:            [],
  error:           null,
  loaded:          false,
  pendingRequests: 0,
}

export function createCrudReducer(entity, actionHandlerExtensions, initialStateExtensions) {
  const initialState = Object.assign(
    {},
    baseInitialState,
    initialStateExtensions,
  )

  const actionHandlers = Object.assign({
    [`load ${entity}`]: handleLoadOneAction,
    [`load ${pluralize(entity)}`]: handleLoadAllAction,
    [`create ${entity}`]: handleCreateAction,
    [`delete ${entity}`]: handleDeleteAction,
  }, actionHandlerExtensions)

  return handleActions(actionHandlers, initialState)
}

function handleLoadOneAction(state, action) {
  if (action.error) {
    return Object.assign({}, state, {
      error: action.payload,
      pendingRequests: decrementedPendingRequests(state),
    })
  }

  return Object.assign({}, state, {
    list: state.list.map(item => {
      if (item.id === action.payload.id) {
        return action.payload
      }

      return item
    }),
    error: null,
    pendingRequests: decrementedPendingRequests(state),
  })
}

function handleLoadAllAction(state, action) {
  if (action.error) {
    return Object.assign({}, state, {
      error: action.payload,
      pendingRequests: decrementedPendingRequests(state),
    })
  }

  return Object.assign({}, state, {
    list: action.payload,
    error: null,
    loaded: true,
    pendingRequests: decrementedPendingRequests(state),
  })
}

function handleCreateAction(state, action) {
  if (action.error) {
    return Object.assign({}, state, {
      error: action.payload,
      pendingRequests: decrementedPendingRequests(state),
    })
  }

  return Object.assign({}, state, {
    list: [...state.list, action.payload],
    error: null,
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
    list: state.list.filter(item => item.id !== action.payload.deletedRecordId),
    error: null,
    pendingRequests: decrementedPendingRequests(state),
  })
}

function decrementedPendingRequests(state) {
  return state.pendingRequests > 0 ? (state.pendingRequests - 1) : 0
}
