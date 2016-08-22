
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
import { formatDateCreated } from './helpers'

const sourceColumnStyle       = { width: '10rem' }
const categoryColumnStyle     = { width: '22rem' }
const difficultyColumnStyle   = { width: '10rem' }
const sourcePageColumnStyle   = { width: '5rem' }
const dateCreatedColumnStyle  = { width: '10rem' }
const questionTypeColumnStyle = { width: '10rem' }

const QuestionTable = (props) => (
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
          </TableRow>
        </TableHeader>
        <TableBody>
          {props.questions.map(question => {
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
            const formattedQuestionDateCreated = formatDateCreated(questionDateCreated)

            return (
              <TableRow key={questionId}>
                <TableRowColumn style={categoryColumnStyle}>{questionCategoryName}</TableRowColumn>
                <TableRowColumn style={difficultyColumnStyle}>{questionDifficultyName}</TableRowColumn>
                <TableRowColumn style={dateCreatedColumnStyle}>{formattedQuestionDateCreated}</TableRowColumn>
                <TableRowColumn style={questionTypeColumnStyle}>{questionType}</TableRowColumn>
                <TableRowColumn style={sourceColumnStyle}>{questionSource ? questionSource : '-'}</TableRowColumn>
                <TableRowColumn style={sourcePageColumnStyle}>{questionSourcePage ? questionSourcePage : '-'}</TableRowColumn>
              </TableRow>
            )
          })}
        </TableBody>
      </Table>
    </div>
  </div>
)

export default QuestionTable
