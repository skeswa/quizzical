
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

import LoginPage from 'containers/LoginPage'
import SplashPage from 'containers/SplashPage'
import QuizzesPage from 'containers/QuizzesPage'
import NotFoundPage from 'containers/NotFoundPage'
import QuizTakePage from 'containers/QuizTakePage'
import QuestionsPage from 'containers/QuestionsPage'
import WorkbenchPage from 'containers/WorkbenchPage'
import QuizAttemptsPage from 'containers/QuizAttemptsPage'
import QuizGenerationPage from 'containers/QuizGenerationPage'

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
            <Route path="start" component={QuizGenerationPage} />
            <Route path=":quizId/take" component={QuizTakePage} />
          </Route>
          <Route path="workbench" component={WorkbenchPage} />
          <Route path="login" component={LoginPage} />
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
}

// Get session information before rendering the UI.
store.dispatch(AuthActions.whoami()).then(renderUI)
