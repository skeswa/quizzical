
import React, { Component } from 'react'

import style from './style.css'
import { Cell } from 'components/Table'

const emptyCellText = '-'

const TableTextCell = props => {
  const { data, columnKey, rowIndex, serializer, ...otherProps } = props
  const rawCellValue = data[rowIndex][columnKey]
  const serializedCellValue = serializer
    ? serializer(rawCellValue)
    : rawCellValue
  const formattedCellValue = serializedCellValue
    ? serializedCellValue
    : emptyCellText

  return (
    <Cell {...otherProps}>{formattedCellValue}</Cell>
  )
}

export default TableTextCell
