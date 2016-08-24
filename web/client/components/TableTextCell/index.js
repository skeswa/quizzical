
import React, { Component } from 'react'
import { Cell } from 'fixed-data-table'

import style from './style.css'

const TableTextCell = props => {
  const { data, columnKey, rowIndex, serializer, ...otherProps } = props
  const rawCellValue = data[rowIndex][columnKey]
  const serializedCellValue = serializer
    ? serializer(rawCellValue)
    : rawCellValue

  return (
    <Cell {...otherProps}>{serializedCellValue}</Cell>
  )
}

export default TableTextCell
