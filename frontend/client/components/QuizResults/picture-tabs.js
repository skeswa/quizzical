
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import { Tabs, Tab } from 'material-ui/Tabs'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import React, { Component } from 'react'

import QuestionPicture from 'components/QuestionPicture'

const TABS_STYLE = { display: 'flex', flex: 1, flexDirection: 'column' }
const TABS_THEME = getMuiTheme({
  palette: {
    textColor: '#444444',
    accent1Color: '#444444',
    primary1Color: '#ffffff',
    alternateTextColor: '#444444',
  }
})
const TABS_CONTENT_STYLE = { display: 'flex', flex: 1 }
const TABS_TEMPLATE_STYLE = { display: 'flex', flex: 1, overflow: 'none' }

class PictureTabs extends Component {
  static propTypes = {
    answerPictureId:    React.PropTypes.number.isRequired,
    questionPictureId:  React.PropTypes.number.isRequired,
  }

  render() {
    const { answerPictureId, questionPictureId } = this.props

    return (
      <MuiThemeProvider muiTheme={TABS_THEME}>
        <Tabs
          style={TABS_STYLE}
          tabTemplate={TabTemplate}
          contentContainerStyle={TABS_CONTENT_STYLE}>
          <Tab label="Question">
            <QuestionPicture pictureId={questionPictureId} />
          </Tab>
          <Tab label="Explanation">
            <QuestionPicture pictureId={answerPictureId} />
          </Tab>
        </Tabs>
      </MuiThemeProvider>
    )
  }
}

class TabTemplate extends Component {
  static propTypes = {
    style:    React.PropTypes.object,
    children: React.PropTypes.node,
    selected: React.PropTypes.bool,
  }

  render() {
    const { children, selected } = this.props
    const templateStyle = Object.assign(
      {},
      TABS_TEMPLATE_STYLE,
      selected
        ? { display: 'none' }
        : null)

    return <div style={templateStyle}>{children}</div>
  }
}

export default PictureTabs
