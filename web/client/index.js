
import { Router, Route, browserHistory } from 'react-router'
import { Provider as ReduxProvider } from 'react-redux'
import { syncHistoryWithStore } from 'react-router-redux'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import ReactDOM from 'react-dom'
import React from 'react'
import configure from './store'

import App from 'containers/App'
import QuestionListPage from 'components/QuestionListPage'

const store = configure()
const history = syncHistoryWithStore(browserHistory, store)

ReactDOM.render(
  <MuiThemeProvider>
    <ReduxProvider store={store}>
      <Router history={history}>
        <Route path="/" component={App}>
          <Route path="questions" component={QuestionListPage} />
        </Route>
      </Router>
    </ReduxProvider>
  </MuiThemeProvider>,
  document.getElementById('root')
)
