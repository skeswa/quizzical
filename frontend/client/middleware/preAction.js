
export default store => next => action => {
  // Look for pre-actions in the meta of the action.
  if (action.meta && action.meta.preAction) {
    store.dispatch(action.meta.preAction)
  }

  return next(action)
}
