import services from 'services/user-quiz'
import { createAction } from 'redux-actions'

const UserQuizActions = {
  loadLatestQuiz: createAction(
    `get latest user quiz`,
    services.latest),
  loadRecentQuizzes: createAction(
    `get recent user quizzes`,
    services.recent),
  loadUnsubmittedQuizzes: createAction(
    `get unsubmitted user quizzes`,
    services.unsubmitted),
}

export default UserQuizActions
