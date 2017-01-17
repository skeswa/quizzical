
import { handleActions } from 'redux-actions'

import Session from 'utils/session'

import {
  incrementedPendingRequests,
  decrementedPendingRequests,
} from './helpers/crud'

const session = Session.retrieve()
const initialState = {
  user:             session ? session.user : null,
  token:            session ? session.token : null,
  authed:           !!session,
  pendingRequests:  0,
}

const AuthReducer = handleActions(
  {
    'load authed user': function(state, action) {
      if (action.error) {
        return Object.assign({}, state, {
          pendingRequests: decrementedPendingRequests(state),
        })
      }

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
