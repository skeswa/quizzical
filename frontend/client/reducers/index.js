
import { routerReducer as routing } from 'react-router-redux'
import { combineReducers } from 'redux'

import auth from './auth'
import category from './category'
import difficulty from './difficulty'
import question from './question'
import quizSubmission from './quiz-submission'
import quiz from './quiz'
import source from './source'

export default combineReducers({
  auth,
  category,
  difficulty,
  question,
  quizSubmission,
  quiz,
  routing,
  source,
})
