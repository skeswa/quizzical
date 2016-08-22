
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

const QuestionTable = (props) => (
  <div className={style.tableWrapper}>
    <div className={style.table}>
      <Table height="100%" style={{ borderBottom: '1px solid #e0e0e0' }}>
        <TableHeader>
          <TableRow>
            <TableHeaderColumn>Category</TableHeaderColumn>
            <TableHeaderColumn>Difficulty</TableHeaderColumn>
            <TableHeaderColumn>Question Type</TableHeaderColumn>
            <TableHeaderColumn>Date Created</TableHeaderColumn>
            <TableHeaderColumn>Source</TableHeaderColumn>
            <TableHeaderColumn>Source Page</TableHeaderColumn>
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

            return (
              <TableRow key={questionId}>
                <TableRowColumn>{questionCategoryName}</TableRowColumn>
                <TableRowColumn>{questionDifficultyName}</TableRowColumn>
                <TableRowColumn>{questionDateCreated}</TableRowColumn>
                <TableRowColumn>{isQuestionMultipleChoice ? 'Multiple Choice' : 'Numeric Answer'}</TableRowColumn>
                <TableRowColumn>{questionSource}</TableRowColumn>
                <TableRowColumn>{questionSourcePage}</TableRowColumn>
              </TableRow>
            )
          })}
        </TableBody>
      </Table>
    </div>
  </div>
)

export default QuestionTable
