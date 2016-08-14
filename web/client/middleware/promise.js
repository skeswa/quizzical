
import { isFSA } from 'flux-standard-action';

function isPromise(val) {
  return val && typeof val.then === 'function';
}

function nextAction(originalAction, result, isError) {
  // Create copy of the original action with a modified payload.
  const newAction = { ...originalAction, payload: result };

  // Assign the error flag if necessary.
  if (isError) {
    newAction.error = true;
  }

  // Make sure pre-action is is stripped.
  if (newAction.meta && newAction.meta.preAction) {
    const metaClone = Object.assign({}, newAction.meta)
    delete metaClone.preAction
    newAction.meta = metaClone
  }

  return newAction
}

export default function promiseMiddleware({ dispatch }) {
  return next => action => {
    if (!isFSA(action)) {
      return isPromise(action)
        ? action.then(dispatch)
        : next(action);
    }

    return isPromise(action.payload)
      ? action.payload.then(
          result => dispatch(nextAction(action, result)),
          error => dispatch(nextAction(action, error, true)),
        )
      : next(action);
  };
}
