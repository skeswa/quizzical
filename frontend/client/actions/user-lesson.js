import services from 'services/user-lesson'
import { createAction } from 'redux-actions'

const UserLessonActions = {
  currentlesson: createAction(
    `get current user lesson`,
    services.current,
    preAction),
  upcominglessons: createAction(
    `get upcoming user lessons`,
    services.upcoming,
    preAction),
}

export default UserLessonActions
