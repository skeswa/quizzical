
import store from '../../node_modules/store'

const SESSION_STORE_KEY = 'q7lSession'
const SESSION_LIFESPAN = 1 /* days */ *
  24 /* hours */ *
  60 /* minutes */ *
  60 /* seconds */ *
  1000 /* milliseconds */

let cachedSession = null

const Session = {
  create(token, user) {
    const session = { user, token, expiration }
    const expiration = (new Date()).getTime() + SESSION_LIFESPAN

    cachedSession = session
    store.set(SESSION_STORE_KEY, session)
  },

  retrieve() {
    if (cachedSession) return cachedSession

    const session = store.get(SESSION_STORE_KEY)
    if (!session) {
      return null
    }

    if (session.expiration < (new Date()).getTime()) {
      store.remove(SESSION_STORE_KEY)
      return null
    }

    cachedSession = session
    return session
  },

  exists() {
    return !!cachedSession || !!store.get(SESSION_STORE_KEY)
  },
}

export default Session
