
import FontIcon from 'material-ui/FontIcon'
import IconButton from 'material-ui/IconButton'
import React, { Component } from 'react'

import style from './style.css'
import { Cell } from 'components/Table'
import { pictureIdToBackgroundURL } from 'utils'

function bindHandler(handler, pictureURL) {
  return () => handler(pictureURL)
}

const TableQuestionPicturesCell = props => {
  const { data, handler, rowIndex, ...otherProps } = props
  const {
    answerPicture: { id: answerPictureId },
    questionPicture: { id: questionPictureId },
  } = data[rowIndex]

  const answerPictureURL    = pictureIdToBackgroundURL(answerPictureId)
  const questionPictureURL  = pictureIdToBackgroundURL(questionPictureId)

  return (
    <Cell {...otherProps}>
      <div className={style.main}>
        <div className={style.label}>Q</div>
        <div className={style.button}>
          <IconButton
            onClick={bindHandler(handler, questionPictureURL)}>
            <FontIcon className="material-icons">photo_library</FontIcon>
          </IconButton>
        </div>
        <div className={style.label}>A</div>
        <div className={style.button}>
          <IconButton
            onClick={bindHandler(handler, answerPictureURL)}>
            <FontIcon className="material-icons">photo_library</FontIcon>
          </IconButton>
        </div>
      </div>
    </Cell>
  )
}

export default TableQuestionPicturesCell
