
import Perf from 'react-addons-perf'
import React from 'react'
import ReactDOM from 'react-dom'
import { AppContainer } from 'react-hot-loader'

import App from 'containers/App'

// Renders the provided component in #root.
const render = Component => {
  console.log('render')
  const root = document.getElementById('root')

  ReactDOM.render(
    <AppContainer>
      <Component />
    </AppContainer>,
    root,
    () => setTimeout(() => root.style.opacity = 1, 100))
}

// Render the application once everything is ready.
render(App)

// Enables hot module reloading.
if (module.hot) {
  module.hot.accept('./containers/App', () => render(App))
}
