import { handleActions } from 'redux-actions'

import Session from 'utils/session'

import {
  incrementedPendingRequests,
  decrementedPendingRequests,
} from './helpers/crud'

const initialState = {
  currentLesson:          {},
  currentLessonLoaded:    false,
  upcomingLessons:        [],
  upcomingLessonsLoaded:  false,
  pendingRequests:        0,
}

const LessonReducer = handleActions(
  {
    'get current user lesson': function(state, action) {
      if (action.error) {
        return Object.assign({}, state, {
          pendingRequests: decrementedPendingRequests(state),
        })
      }

      return Object.assign({}, state, {
        currentLesson: action.payload || {},
        currentLessonLoaded: true,
        pendingRequests: decrementedPendingRequests(state),
      })
    },
    'get upcoming user lessons': function(state, action) {
      if (action.error) {
        return Object.assign({}, state, {
          pendingRequests: decrementedPendingRequests(state),
        })
      }

      return Object.assign({}, state, {
        upcomingLessons: action.payload || [],
        upcomingLessonsLoaded: true,
        pendingRequests: decrementedPendingRequests(state),
      })
    },
  },
  initialState)

export default LessonReducer
