
import Dialog from 'material-ui/Dialog'
import ReactDOM from 'react-dom'
import FontIcon from 'material-ui/FontIcon'
import IconButton from 'material-ui/IconButton'
import React, { Component } from 'react'
import { Table, Column, Cell } from 'fixed-data-table'

import style from './style.css'
import tableStyles from 'fixed-data-table/dist/fixed-data-table.min.css'
import TableTextCell from 'components/TableTextCell'
import TableHeaderCell, { SortTypes } from 'components/TableHeaderCell'
import { formatDateCreated, pictureNameToBackgroundURL } from 'utils'

class QuestionTable extends Component {
  state = {
    width:            null,
    height:           null,
    lightboxVisible:  false,
    lightboxImageURL: null,
  }

  componentDidMount() {
    // Calculate the width of this div so the table might be rendered.
    const mainDOMNode = ReactDOM.findDOMNode(this.refs.main)
    const clientWidth = mainDOMNode.clientWidth
    const clientHeight = mainDOMNode.clientHeight

    this.setState({ width: clientWidth, height: clientHeight })
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
        rowHeight={40}
        rowsCount={questions.length}
        headerHeight={40}
        width={width}
        height={height}>
        <Column
          columnKey="category"
          header={<TableHeaderCell title="Category" />}
          width={220}
          cell={
            <TableTextCell
              data={questions}
              serializer={category => category.name} />
          } />
        <Column
          columnKey="difficulty"
          header={<TableHeaderCell title="Difficulty" />}
          width={80}
          cell={
            <TableTextCell
              data={questions}
              serializer={difficulty => difficulty.name} />
          } />
        <Column
          columnKey="requiresCalculator"
          header={<TableHeaderCell title="Requires Calculator" />}
          width={120}
          cell={
            <TableTextCell
              data={questions}
              serializer={requiresCalculator => requiresCalculator ? 'Yes' : 'No'} />
          } />
        <Column
          columnKey="dateCreated"
          header={<TableHeaderCell title="Date Created" />}
          width={120}
          cell={
            <TableTextCell
              data={questions}
              serializer={dateCreated => formatDateCreated(dateCreated)} />
          } />
        <Column
          columnKey="multipleChoice"
          header={<TableHeaderCell title="Question Type" />}
          width={100}
          cell={
            <TableTextCell
              data={questions}
              serializer={multipleChoice => multipleChoice ? 'Multiple Choice' : 'Numeric Answer'} />
          } />
        <Column
          columnKey="source"
          header={<TableHeaderCell title="Source" />}
          width={120}
          cell={
            <TableTextCell data={questions} />
          } />
        <Column
          columnKey="sourcePage"
          header={<TableHeaderCell title="Source Page" />}
          width={80}
          cell={
            <TableTextCell data={questions} />
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
