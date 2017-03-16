
import { Route, Redirect } from 'react-router-dom'
import React, { PropTypes } from 'react'

import Session from 'utils/session'

const PrivateRoute =
    ({ component, ...rest }) =>
        <Route
          {...rest}
          render={
              props =>
                  Session.exists()
                      ? React.createElement(component, props)
                      : <Redirect
                          to={{
                            pathname: '/login',
                            state: { from: props.location },
                          }} />
          } />

export default PrivateRoute
