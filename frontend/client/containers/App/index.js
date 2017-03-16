
/* Core UI Framework */
import React, { Component } from 'react'

/* Routing */
import createHistory from 'history/createBrowserHistory'
import { Route, Switch } from 'react-router-dom'
import { ConnectedRouter, routerMiddleware } from 'react-router-redux'

/* App-wide State Management */
import configureStore from '../../store'
import { Provider as StoreProvider } from 'react-redux'

/* Component Library */
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import injectTapEventPlugin from 'react-tap-event-plugin'

/* App Pages */
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

/* Authorization */
import Session from 'utils/session'

/* Global Styles */
import style from './index.css'

// Add support for onTouch events.
injectTapEventPlugin()
// Create a history of your choosing.
const history = createHistory()
// Build the store using router middleware.
const store = configureStore(routerMiddleware(history))
// Theme for Material UI.
const muiTheme = getMuiTheme({
  palette: {
    primary1Color: '#754aec',
    primary2Color: '#651FFF',
    primary3Color: '#BDBDBD',
  }
})

const App = () =>
    <MuiThemeProvider muiTheme={muiTheme}>
      <StoreProvider store={store}>
        <ConnectedRouter history={history}>
          <Switch>
            {/* Admin pages */}
            <Route exact path="/admin/quizzes" component={QuizzesPage} />
            <Route exact path="/admin/attempts" component={QuizAttemptsPage} />
            <Route exact path="/admin/questions" component={QuestionsPage} />

            {/* Student pages */}
            <Route exact path="/quiz" component={QuizAutoGenerationPage} />
            <Route exact path="/qa/quiz" component={QuizGenerationQAPage} />
            <Route exact path="/quiz/:id" component={QuizTakePage} />
            <Route exact path="/quiz/:id/results" component={QuizResultsPage} />

            {/* Misc. pages */}
            <Route exact path="/login" component={LoginPage} />
            <Route exact path="/workbench" component={WorkbenchPage} />
            <Route exact path="/" component={SplashPage} />
            <Route path="" component={NotFoundPage} />
          </Switch>
        </ConnectedRouter>
      </StoreProvider>
    </MuiThemeProvider>

export default App
