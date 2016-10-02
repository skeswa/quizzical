
import Dialog from 'material-ui/Dialog'
import ReactDOM from 'react-dom'
import FontIcon from 'material-ui/FontIcon'
import IconButton from 'material-ui/IconButton'
import React, { Component } from 'react'
import elementResizeDetectorMaker from 'element-resize-detector'

import style from './style.css'
import TableTextCell from 'components/TableTextCell'
import { Table, Column, Cell } from 'components/Table'
import TableQuestionPicturesCell from 'components/TableQuestionPicturesCell'
import TableHeaderCell, { SortTypes } from 'components/TableHeaderCell'
import { formatDateCreated, pictureNameToBackgroundURL } from 'utils'

// 16.667 * 5 = 5 frames.
const tableResizeDelay = 16.667 * 5

class QuestionTable extends Component {
  state = {
    erd:              null,
    width:            null,
    height:           null,
    lightboxVisible:  false,
    lightboxImageURL: null,
  }

  componentDidMount() {
    // Calculate the width of this div so the table might be rendered.
    this.onResizeWithCallback(el => {
      let ref   = null
      let erd   = elementResizeDetectorMaker({ strategy: 'scroll' })

      // Start listening for resizing.
      erd.listenTo(el, () => {
        if (ref) {
          clearTimeout(ref)
        }

        ref = setTimeout(::this.onResize, tableResizeDelay)
      })

      // Keep the erd for unsubscription.
      this.setState({ erd })
    })
  }

  componentWillUnmount() {
    const erd = this.state.erd
    if (erd) {
      erd.removeAllListeners(ReactDOM.findDOMNode(this.refs.main))
    }
  }

  onResize() {
    this.onResizeWithCallback(undefined)
  }

  onResizeWithCallback(callback) {
    const mainDOMNode   = ReactDOM.findDOMNode(this.refs.main)
    const clientWidth   = mainDOMNode.clientWidth
    const clientHeight  = mainDOMNode.clientHeight

    this.setState(
      { width: clientWidth, height: clientHeight },
      callback ? callback.bind(this, mainDOMNode) : undefined,
    )
  }

  onLightboxOpened(lightboxImageURL) {
    this.setState({
      lightboxImageURL,
      lightboxVisible: true,
    })
  }

  onLightboxClosed() {
    this.setState({
      lightboxVisible:  false,
      lightboxImageURL: null,
    })
  }

  renderLightbox() {
    const { lightboxVisible, lightboxImageURL } = this.state

    return (
      <Dialog
        open={lightboxVisible}
        bodyStyle={{ padding: '0' }}
        contentStyle={{ width: '50%', minWidth: '0', maxWidth: 'none', maxHeight: '80%' }}
        overlayStyle={{ paddingTop: '0' }}
        onRequestClose={::this.onLightboxClosed}>
        <img
          src={lightboxImageURL}
          className={style.lightboxPicture} />
      </Dialog>
    )
  }

  renderTable() {
    const { questions } = this.props
    const { width, height } = this.state

    // Exit if there is no size for the table yet.
    if (!width || !height) {
      return null
    }

    return (
      <Table
        rowHeight={48}
        rowsCount={questions.length}
        headerHeight={56}
        width={width}
        height={height}>
        <Column
          columnKey="category"
          header={<TableHeaderCell title="Category" />}
          width={255}
          cell={
            <TableTextCell
              data={questions}
              serializer={category => category.name} />
          } />
        <Column
          columnKey="difficulty"
          header={<TableHeaderCell title="Difficulty" />}
          width={115}
          cell={
            <TableTextCell
              data={questions}
              serializer={difficulty => difficulty.name} />
          } />
        <Column
          columnKey="requiresCalculator"
          header={<TableHeaderCell title="Requires Calculator" />}
          width={155}
          cell={
            <TableTextCell
              data={questions}
              serializer={requiresCalculator => requiresCalculator ? 'Yes' : 'No'} />
          } />
        <Column
          columnKey="dateCreated"
          header={<TableHeaderCell title="Date Created" />}
          width={155}
          cell={
            <TableTextCell
              data={questions}
              serializer={dateCreated => formatDateCreated(dateCreated)} />
          } />
        <Column
          columnKey="multipleChoice"
          header={<TableHeaderCell title="Question Type" />}
          width={135}
          cell={
            <TableTextCell
              data={questions}
              serializer={multipleChoice => multipleChoice ? 'Multiple Choice' : 'Numeric Answer'} />
          } />
        <Column
          columnKey="source"
          header={<TableHeaderCell title="Source" />}
          width={155}
          cell={
            <TableTextCell
              data={questions}
              serializer={source => source.name} />
          } />
        <Column
          columnKey="sourcePageNumber"
          header={<TableHeaderCell title="Source Page" />}
          width={115}
          cell={
            <TableTextCell data={questions} />
          } />
        <Column
          columnKey="sourceIndexWithinPage"
          header={<TableHeaderCell title="Index in Page" />}
          width={90}
          cell={
            <TableTextCell data={questions} />
          } />
        <Column
          columnKey="pictures"
          header={<TableHeaderCell title="Pictures" />}
          width={125}
          cell={
            <TableQuestionPicturesCell
              data={questions}
              handler={::this.onLightboxOpened} />
          } />
      </Table>
    )
  }

  render() {
    return (
      <div className={style.wrapper}>
        <div ref="main" className={style.main}>
          {this.renderTable()}
        </div>

        {this.renderLightbox()}
      </div>
    )
  }
}

export default QuestionTable
