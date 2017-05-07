
/* Core UI Framework */
import React, { Component } from 'react'

/* Routing */
import { Route, Switch } from 'react-router-dom'
import { ConnectedRouter } from 'react-router-redux'

/* Routing Helpers */
import PublicRoute from 'components/PublicRoute'
import PrivateRoute from 'components/PrivateRoute'

/* State Management */
import { Provider } from 'react-redux'

/* Component Library */
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'

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
import StudentLessonsPage from 'containers/StudentLessonsPage'
import QuizAutoGenerationPage from 'containers/QuizAutoGenerationPage'
import QuizGenerationQAPage from 'containers/QuizGenerationQAPage'

/* Global Styles */
import style from './index.css'

const App = props =>
    <Provider store={props.store}>
      <MuiThemeProvider muiTheme={props.muiTheme}>
        <ConnectedRouter history={props.history}>
          <Switch>
            {/* Admin pages */}
            <PrivateRoute exact path="/admin/quizzes" component={QuizzesPage} />
            <PrivateRoute exact path="/admin/attempts" component={QuizAttemptsPage} />
            <PrivateRoute exact path="/admin/questions" component={QuestionsPage} />

            {/* Student pages */}
            <PrivateRoute exact path="/lessons" component={StudentLessonsPage} />
            <PrivateRoute exact path="/quiz" component={QuizAutoGenerationPage} />
            <PrivateRoute exact path="/qa/quiz" component={QuizGenerationQAPage} />
            <PrivateRoute exact path="/quiz/:id" component={QuizTakePage} />
            <PrivateRoute exact path="/quiz/:id/results" component={QuizResultsPage} />

            {/* Misc. pages */}
            <PublicRoute exact path="/login" component={LoginPage} />
            <PrivateRoute exact path="/workbench" component={WorkbenchPage} />
            <Route exact path="/" component={SplashPage} />
            <Route path="" component={NotFoundPage} />
          </Switch>
        </ConnectedRouter>
      </MuiThemeProvider>
    </Provider>

export default App
