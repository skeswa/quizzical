
import { createAction } from 'redux-actions'

import services from 'services/auth'

const bumpPendingRequests = createAction(`bump pending auth requests`)
const preAction = () => ({ preAction: bumpPendingRequests() })

const AuthActions = {
  login: createAction(`load authed user`, services.login, preAction),
  whoami: createAction(`load authed user`, services.whoami, preAction),
}

export default AuthActions
