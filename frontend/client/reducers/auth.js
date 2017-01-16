
import { handleActions } from 'redux-actions'

import {
  incrementedPendingRequests,
  decrementedPendingRequests,
} from './helpers/crud'

const initialState = {
  user:             null,
  authed:           false,
  pendingRequests:  0,
}

const AuthReducer = handleActions(
  {
    'load authed user': function(state, action) {
      return Object.assign({}, state, {
        user: action.payload,
        authed: true,
        pendingRequests: decrementedPendingRequests(state),
      })
    },
    'bump pending auth requests': function(state, action) {
      return Object.assign({}, state, {
        pendingRequests: incrementedPendingRequests(state),
      })
    },
  },
  initialState)

export default AuthReducer
