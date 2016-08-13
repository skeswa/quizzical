package common

import (
	"bytes"
	"database/sql"

	// Enable usage of postgres connector.
	_ "github.com/lib/pq"
)

func StartDBConnection(config *Config) (*sql.DB, error) {
	connStrBuffer := bytes.Buffer{}
	connStrBuffer.WriteString("host=")
	connStrBuffer.WriteString(config.DbAddress)
	connStrBuffer.WriteString(" user=")
	connStrBuffer.WriteString(config.DbUser)
	connStrBuffer.WriteString(" password=")
	connStrBuffer.WriteString(config.DbPass)
	connStrBuffer.WriteString(" dbname=")
	connStrBuffer.WriteString(config.DbName)
	connStrBuffer.WriteString(" sslmode=disable")

	return sql.Open("postgres", connStrBuffer.String())
}
