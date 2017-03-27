
/* Core UI Framework */
import Perf from 'react-addons-perf'
import ReactDOM from 'react-dom'
import React, { Component } from 'react'

/* Development tooling */
import { AppContainer } from 'react-hot-loader'

/* Component Library */
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import injectTapEventPlugin from 'react-tap-event-plugin'

/* Routing */
import createHistory from 'history/createBrowserHistory'
import { routerMiddleware } from 'react-router-redux'

/* State Management */
import configureStore from './store'

/* Entry Point */
import App from 'containers/App'

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

// Renders the provided root component in #root.
const renderRoot = Root => {
  const root = document.getElementById('root')

  ReactDOM.render(
    <AppContainer>
      <Root
        store={store}
        history={history}
        muiTheme={muiTheme} />
    </AppContainer>,
    root,
    // Fade in the root element if it is invisible.
    () => setTimeout(() => root.style.opacity = 1, 100))
}

// Enables hot module reloading in dev.
if (module.hot) {
  module.hot.accept('containers/App', () => {
    // We need to get the next version of the App since webpack was updated it.
    const NextApp = require('containers/App').default
    // Force a component tree update.
    renderRoot(NextApp)
  })
}

// Start the application.
renderRoot(App)
