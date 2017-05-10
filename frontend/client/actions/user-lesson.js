import services from 'services/user-lesson'
import { createAction } from 'redux-actions'

const UserLessonActions = {
  loadCurrentLesson: createAction(
    `get current user lesson`,
    services.current),
  loadUpcomingLessons: createAction(
    `get upcoming user lessons`,
    services.upcoming),
  loadFinishedLessons: createAction(
    `get finished user lessons`,
    services.finished),    
}

export default UserLessonActions
