
import { routerReducer as routing } from 'react-router-redux'
import { combineReducers } from 'redux'

import auth from './auth'
import category from './category'
import difficulty from './difficulty'
import question from './question'
import quizSubmission from './quiz-submission'
import quizGeneration from './quiz-generation'
import quiz from './quiz'
import source from './source'
import userLesson from './user-lesson'
import userQuiz from './user-quiz'
import lessonStatus from './lesson-status'
import lessonType from './lesson-type'

export default combineReducers({
  auth,
  category,
  difficulty,
  question,
  quizSubmission,
  quizGeneration,
  quiz,
  routing,
  source,
  userLesson,
  userQuiz,
  lessonStatus,
  lessonType,
})
