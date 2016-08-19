
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
import QuizzesPage from 'containers/QuizzesPage'
import PracticePage from 'containers/PracticePage'
import QuestionsPage from 'containers/QuestionsPage'

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
          <Route path="quizzes" component={QuizzesPage} />
          <Route path="practice" component={PracticePage} />
          <Route path="questions" component={QuestionsPage} />
        </Route>
      </Router>
    </ReduxProvider>
  </MuiThemeProvider>,
  document.getElementById('root')
)
