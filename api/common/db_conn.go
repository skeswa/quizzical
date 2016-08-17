package common

import (
	"bytes"
	"database/sql"

	// Enable usage of postgres connector.
	_ "github.com/lib/pq"
)

const (
	maxDBConnections = 10
)

// StartDBConnection opens a connection to the database and performs some
// initializations.
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

	db, err := sql.Open("postgres", connStrBuffer.String())
	if err != nil {
		return nil, err
	}

	// Make sure we don't overrun the poor database.
	db.SetMaxOpenConns(maxDBConnections)

	return db, nil
}
