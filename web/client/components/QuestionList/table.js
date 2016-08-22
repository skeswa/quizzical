
import Dialog from 'material-ui/Dialog'
import FontIcon from 'material-ui/FontIcon'
import IconButton from 'material-ui/IconButton'
import React, { Component } from 'react'
import {
  Table,
  TableRow,
  TableBody,
  TableHeader,
  TableRowColumn,
  TableHeaderColumn,
} from 'material-ui/Table'

import style from './style.css'
import { formatDateCreated, pictureNameToBackgroundUrl } from './helpers'

const sourceColumnStyle           = { width: '10rem' }
const categoryColumnStyle         = { width: '22rem' }
const difficultyColumnStyle       = { width: '10rem' }
const sourcePageColumnStyle       = { width: '5rem' }
const dateCreatedColumnStyle      = { width: '10rem' }
const questionTypeColumnStyle     = { width: '10rem' }
const questionPictureColumnStyle  = { width: '5rem' }

class QuestionTable extends Component {
  state = {
    lightboxVisible:  false,
    lightboxImageURL: null,
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

  render() {
    return (
      <div className={style.tableWrapper}>
        <div className={style.table}>
          <Table height="100%" style={{ borderBottom: '1px solid #e0e0e0' }}>
            <TableHeader>
              <TableRow>
                <TableHeaderColumn style={categoryColumnStyle}>Category</TableHeaderColumn>
                <TableHeaderColumn style={difficultyColumnStyle}>Difficulty</TableHeaderColumn>
                <TableHeaderColumn style={dateCreatedColumnStyle}>Date Created</TableHeaderColumn>
                <TableHeaderColumn style={questionTypeColumnStyle}>Question Type</TableHeaderColumn>
                <TableHeaderColumn style={sourceColumnStyle}>Source</TableHeaderColumn>
                <TableHeaderColumn style={sourcePageColumnStyle}>Page</TableHeaderColumn>
                <TableHeaderColumn style={questionPictureColumnStyle}></TableHeaderColumn>
              </TableRow>
            </TableHeader>
            <TableBody>
              {this.props.questions.map(question => {
                const {
                  id: questionId,
                  source: questionSource,
                  category: { name: questionCategoryName },
                  difficulty: {
                    name: questionDifficultyName,
                    color: questionDifficultyColor,
                  },
                  sourcePage: questionSourcePage,
                  dateCreated: questionDateCreated,
                  multipleChoice: isQuestionMultipleChoice,
                  questionPicture: questionPicture,
                } = question

                const questionType = isQuestionMultipleChoice
                  ? 'Multiple Choice'
                  : 'Numeric Answer'
                const questionPictureURL            = pictureNameToBackgroundUrl(questionPicture)
                const formattedQuestionDateCreated  = formatDateCreated(questionDateCreated)

                return (
                  <TableRow key={questionId}>
                    <TableRowColumn style={categoryColumnStyle}>{questionCategoryName}</TableRowColumn>
                    <TableRowColumn style={difficultyColumnStyle}>{questionDifficultyName}</TableRowColumn>
                    <TableRowColumn style={dateCreatedColumnStyle}>{formattedQuestionDateCreated}</TableRowColumn>
                    <TableRowColumn style={questionTypeColumnStyle}>{questionType}</TableRowColumn>
                    <TableRowColumn style={sourceColumnStyle}>{questionSource ? questionSource : '-'}</TableRowColumn>
                    <TableRowColumn style={sourcePageColumnStyle}>{questionSourcePage ? questionSourcePage : '-'}</TableRowColumn>
                      <TableRowColumn style={questionPictureColumnStyle}>
                        <IconButton onClick={this.onLightboxOpened.bind(this, questionPictureURL)}>
                          <FontIcon className="material-icons">collections</FontIcon>
                        </IconButton>
                      </TableRowColumn>
                  </TableRow>
                )
              })}
            </TableBody>
          </Table>
        </div>

        {this.renderLightbox()}
      </div>
    )
  }
}

export default QuestionTable
