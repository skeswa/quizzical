
import pluralize from 'pluralize'
import { handleActions } from 'redux-actions'

const baseInitialState = {
  list:            [],
  loaded:          false,
  createError:    null,
  deleteError:    null,
  loadOneError:    null,
  loadAllError:    null,
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
    [`bump pending ${entity} requests`]: handleBumpPendingRequestsAction,
  }, actionHandlerExtensions)

  return handleActions(actionHandlers, initialState)
}

function handleLoadOneAction(state, action) {
  if (action.error) {
    return Object.assign({}, state, {
      loadOneError: action.payload,
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
    loadOneError: null,
    pendingRequests: decrementedPendingRequests(state),
  })
}

function handleLoadAllAction(state, action) {
  if (action.error) {
    return Object.assign({}, state, {
      loadAllError: action.payload,
      pendingRequests: decrementedPendingRequests(state),
    })
  }

  return Object.assign({}, state, {
    list: action.payload,
    loaded: true,
    loadAllError: null,
    pendingRequests: decrementedPendingRequests(state),
  })
}

function handleCreateAction(state, action) {
  if (action.error) {
    return Object.assign({}, state, {
      createError: action.payload,
      pendingRequests: decrementedPendingRequests(state),
    })
  }

  return Object.assign({}, state, {
    list: [...state.list, action.payload],
    createError: null,
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
    deleteError: null,
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
