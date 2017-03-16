
import { createStore, applyMiddleware } from 'redux'

import reducers from '../reducers'
import { logger, promise, preAction } from '../middleware'

export default function configure(routerMiddleware) {
  // Enable redux devtools.
  const create = window.devToolsExtension
    ? window.devToolsExtension()(createStore)
    : createStore

  // Modify the create store function with middleware.
  const createStoreWithMiddleware = applyMiddleware(
    logger,
    preAction,
    promise,
    routerMiddleware,
  )(create)

  // Initialize the store.
  const store = createStoreWithMiddleware(reducers)

  // Custom logic for the hot loader.
  if (module.hot) {
    module.hot.accept('../reducers', () => {
      const nextReducer = require('../reducers')
      store.replaceReducer(nextReducer)
    })
  }

  return store
}
