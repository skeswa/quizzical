
import debug from 'debug'

const logger = debug('quizzical:redux')

export default store => next => action  => {
  if (action.error) {
    logger(action.type, 'error:', action.error, '\npayload:', action.payload, )
  } else {
    logger(action.type, '\npayload:', action.payload)
  }

  return next(action)
}
