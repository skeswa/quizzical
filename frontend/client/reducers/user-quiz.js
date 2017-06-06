import { handleActions } from 'redux-actions'

import Session from 'utils/session'

import {
  incrementedPendingRequests,
  decrementedPendingRequests,
} from './helpers/crud'

const initialState = {
  latestQuiz:          {},
  latestQuizLoaded:    false,
  recentQuizzes:        [],
  recentQuizzesLoaded:  false,
  unsubmittedQuizzes:        [],
  unsubmittedQuizzesLoaded:  false,
  pendingRequests:        0,
}

const QuizReducer = handleActions(
  {
    'get latest user quiz': function(state, action) {
      if (action.error) {
        return Object.assign({}, state, {
          pendingRequests: decrementedPendingRequests(state),
        })
      }

      return Object.assign({}, state, {
        latestQuiz: action.payload || {},
        latestQuizLoaded: true,
        pendingRequests: decrementedPendingRequests(state),
      })
    },
    'get recent user quizzes': function(state, action) {
      if (action.error) {
        return Object.assign({}, state, {
          pendingRequests: decrementedPendingRequests(state),
        })
      }

      return Object.assign({}, state, {
        recentQuizzes: action.payload || [],
        recentQuizzesLoaded: true,
        pendingRequests: decrementedPendingRequests(state),
      })
    },
    'get unsubmitted user quizzes': function(state, action) {
      if (action.error) {
        return Object.assign({}, state, {
          pendingRequests: decrementedPendingRequests(state),
        })
      }

      return Object.assign({}, state, {
        unsubmittedQuizzes: action.payload || [],
        unsubmittedQuizzesLoaded: true,
        pendingRequests: decrementedPendingRequests(state),
      })
    },
  },
  initialState)

export default QuizReducer
