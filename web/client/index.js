
import { Router, Route, browserHistory } from 'react-router'
import { Provider as ReduxProvider } from 'react-redux'
import { syncHistoryWithStore } from 'react-router-redux'
import injectTapEventPlugin from 'react-tap-event-plugin'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import ReactDOM from 'react-dom'
import React from 'react'
import configure from './store'

import App from 'containers/App'
import PracticePage from 'components/PracticePage'
import QuizListPage from 'components/QuizListPage'
import QuestionListPage from 'components/QuestionListPage'

const store = configure()
const history = syncHistoryWithStore(browserHistory, store)

// Add support for onTouch events.
injectTapEventPlugin()

// Theme for Material UI.
const muiTheme = getMuiTheme({
  palette: {
    primary1Color: '#00796b',
    primary2Color: '#00544A',
    primary3Color: '#BDBDBD',
  }
})

ReactDOM.render(
  <MuiThemeProvider muiTheme={muiTheme}>
    <ReduxProvider store={store}>
      <Router history={history}>
        <Route path="/" component={App}>
          <Route path="quizzes" component={QuizListPage} />
          <Route path="practice" component={PracticePage} />
          <Route path="questions" component={QuestionListPage} />
        </Route>
      </Router>
    </ReduxProvider>
  </MuiThemeProvider>,
  document.getElementById('root')
)
