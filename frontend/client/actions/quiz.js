
import services from 'services/quiz'
import { createAction } from 'redux-actions'
import { createCrudActions } from './helpers/crud'

export default createCrudActions('quiz', services, preAction => ({
  generateQuiz: createAction(
    `create quiz`,
    services.generate,
    preAction),
}))
