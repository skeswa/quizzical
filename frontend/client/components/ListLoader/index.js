
import RefreshIndicator from 'material-ui/RefreshIndicator'
import React, { Component } from 'react'

import style from './style.css'

const ListLoader = (props, context) => (
  <div className={style.main}>
    <RefreshIndicator
      top={0}
      left={0}
      size={50}
      status="loading"
      style={{ position: 'relative' }} />
  </div>
)

export default ListLoader
