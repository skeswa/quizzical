
import { Route, Redirect } from 'react-router-dom'
import React, { PropTypes } from 'react'

import Session from 'utils/session'

const PublicRoute =
    ({ component, ...rest }) =>
        <Route
          {...rest}
          render={
              props =>
                  !Session.exists()
                      ? React.createElement(component, props)
                      : <Redirect
                          to={{
                            pathname: '/quiz',
                            state: { from: props.location },
                          }} />
          } />

export default PublicRoute
