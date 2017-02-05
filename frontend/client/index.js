
import { Route, Router, IndexRedirect, browserHistory } from 'react-router'
import { Provider as ReduxProvider } from 'react-redux'
import { syncHistoryWithStore } from 'react-router-redux'
import injectTapEventPlugin from 'react-tap-event-plugin'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import ReactDOM from 'react-dom'
import Perf from 'react-addons-perf'
import React from 'react'

import style from './index.css'
import configure from './store'
import AuthActions from 'actions/auth'
import Session from 'utils/session'

import LoginPage from 'containers/LoginPage'
import SplashPage from 'containers/SplashPage'
import QuizzesPage from 'containers/QuizzesPage'
import NotFoundPage from 'containers/NotFoundPage'
import QuizTakePage from 'containers/QuizTakePage'
import QuizResultsPage from 'containers/QuizResultsPage'
import QuestionsPage from 'containers/QuestionsPage'
import WorkbenchPage from 'containers/WorkbenchPage'
import QuizAttemptsPage from 'containers/QuizAttemptsPage'
import QuizAutoGenerationPage from 'containers/QuizAutoGenerationPage'
import QuizGenerationQAPage from 'containers/QuizGenerationQAPage'

const store = configure()
const history = syncHistoryWithStore(browserHistory, store)

// Add support for onTouch events.
injectTapEventPlugin()

// Theme for Material UI.
const muiTheme = getMuiTheme({
  palette: {
    primary1Color: '#754aec',
    primary2Color: '#651FFF',
    primary3Color: '#BDBDBD',
  }
})

// Reveals the UI by fading it in. Also, exposes Perf.
function revealUI() {
  root.style.opacity = 1
  window.Perf = Perf
}

// Renders the UI in the #root div.
function renderUI() {
  // Find the root element.
  const root = document.getElementById('root')

  // Render the UI in the root element.
  ReactDOM.render(
    <MuiThemeProvider muiTheme={muiTheme}>
      <ReduxProvider store={store}>
        <Router history={history}>
          <Route path="admin" onEnter={requireAuth}>
            <IndexRedirect to="/admin/questions" />
            <Route path="quizzes" component={QuizzesPage} />
            <Route path="attempts" component={QuizAttemptsPage} />
            <Route path="questions" component={QuestionsPage} />
          </Route>
          <Route path="quiz" onEnter={requireAuth}>
            <IndexRedirect to="/quiz/start" />
            <Route path="start" component={QuizAutoGenerationPage} />
            <Route path="startqa" component={QuizGenerationQAPage} />
            <Route path=":quizId/take" component={QuizTakePage} />
            <Route path="submission/:quizId" component={QuizResultsPage} />
          </Route>
          <Route path="workbench" component={WorkbenchPage} />
          <Route path="login" component={LoginPage} onEnter={requireNoAuth} />
          <Route path="/" component={SplashPage} />
          <Route path="*" component={NotFoundPage} />
        </Router>
      </ReduxProvider>
    </MuiThemeProvider>,
    root,
    () => setTimeout(
      () => root.style.opacity = 1,
      400 /* Reveal delay. */))
}

// Protects <Route>s from unauthenticated users.
function requireAuth(nextState, replace) {
  if (!store.getState().auth.authed) {
    replace({
      pathname: '/login',
      state: { nextPathname: nextState.location.pathname }
    })
  }
  else {//send to QA if qa role
    const session = Session.retrieve()
    if (session.user.qa === true) {
      if (!((nextState.location.pathname === '/quiz/startqa') ||
          nextState.location.pathname.startsWith('/quiz/submission') ||
          nextState.location.pathname.startsWith('/admin')))
        replace({pathname: '/quiz/startqa'})
    }
  }
}

// Prevents authed users from entering the <Route>.
function requireNoAuth(nextState, replace) {
  if (store.getState().auth.authed) {
    replace({ pathname: '/quiz/start' })
  }
}

// Render the application once everything is ready.
renderUI()
