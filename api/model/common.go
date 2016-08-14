package model

import "bytes"

const (
	sqlNow            = "NOW()"
	returningIDClause = `RETURNING "id"`
)

func column(table string, column string) string {
	buffer := bytes.Buffer{}
	buffer.WriteString(table)
	buffer.WriteByte('.')
	buffer.WriteString(column)

	return buffer.String()
}

func on(table string, fromColumn string, toColumn string) string {
	buffer := bytes.Buffer{}
	buffer.WriteString(table)
	buffer.WriteString(" ON ")
	buffer.WriteString(fromColumn)
	buffer.WriteString(" = ")
	buffer.WriteString(toColumn)

	return buffer.String()
}

func lower(column string) string {
	buffer := bytes.Buffer{}
	buffer.WriteString("LOWER(")
	buffer.WriteString(column)
	buffer.WriteByte(')')

	return buffer.String()
}
