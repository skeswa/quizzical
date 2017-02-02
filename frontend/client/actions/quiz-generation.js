
import services from 'services/quiz-generation'
import { createAction } from 'redux-actions'
import { createCrudActions } from './helpers/crud'

export default createCrudActions('quiz-generation', services, preAction => ({
  getQuizGenerationType: createAction(
    `get quiz genation type`,
    services.whattype,
    preAction),
}))
