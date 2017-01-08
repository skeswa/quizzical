
import { Route, Router, IndexRedirect, browserHistory } from 'react-router'
import { Provider as ReduxProvider } from 'react-redux'
import { syncHistoryWithStore } from 'react-router-redux'
import injectTapEventPlugin from 'react-tap-event-plugin'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import ReactDOM from 'react-dom'
import Perf from 'react-addons-perf'
import React from 'react'
import configure from './store'

import LoginPage from 'containers/LoginPage'
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

// Designate the root element.
const root = document.getElementById('root')
// How long in milliseconds to wait before fading in the UI.
const revealDelay = 400
// Reveals the UI by fading it in. Also, exposes Perf.
const revealUI = () => {
  root.style.opacity = 1
  window.Perf = Perf
}

ReactDOM.render(
  <MuiThemeProvider muiTheme={muiTheme}>
    <ReduxProvider store={store}>
      <Router history={history}>
        <Route path="admin">
          <IndexRedirect to="/quizzes" />
          <Route path="quizzes" component={QuizzesPage} />
          <Route path="attempts" component={QuizAttemptsPage} />
          <Route path="questions" component={QuestionsPage} />
        </Route>
        <Route path="quiz">
          <IndexRedirect to="/start" />
          <Route path="start" component={QuizGenerationPage} />
          <Route path=":quizId/take" component={QuizTakePage} />
        </Route>
        <Route path="workbench" component={WorkbenchPage} />
        <Route path="/" component={LoginPage} />
        <Route path="*" component={NotFoundPage} />
      </Router>
    </ReduxProvider>
  </MuiThemeProvider>,
  root,
  () => setTimeout(revealUI, revealDelay))
