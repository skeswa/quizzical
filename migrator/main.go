package main

import (
	"bytes"
	"fmt"
	"log"
	"os"

	_ "github.com/skeswa/migrate/driver/postgres"
	"github.com/skeswa/migrate/migrate"
)

func main() {
	// Localize environment variables.
	dbUser := os.Getenv("GAUNTLET_DB_USER")
	dbPass := os.Getenv("GAUNTLET_DB_PASS")
	dbName := os.Getenv("GAUNTLET_DB_NAME")
	dbAddress := os.Getenv("GAUNTLET_DB_ADDR")
	migrationsPath := os.Getenv("GAUNTLET_MIGRATIONS_PATH")

	// Log the environment for easy debuggability.
	fmt.Printf(`Configuration:

Database name:     %s
Database user:     %s
Database address:  %s
Database password: %s
Migrations path:   %s

`, dbName, dbUser, dbAddress, dbPass, migrationsPath)

	// Execute the migrations.
	log.Println("Executing pending database migrations.")
	err := executeMigrations(dbUser, dbPass, dbAddress, dbName, migrationsPath)
	if err != nil {
		log.Fatalln(err)
	}

	log.Println("Migrations executed successfully.")
}

func executeMigrations(dbUser string, dbPass string, dbAddress string, dbName string, migrationsPath string) error {
	// Create the migrate connection string.
	buffer := bytes.Buffer{}
	buffer.WriteString("postgres://")
	buffer.WriteString(dbUser)
	buffer.WriteByte(':')
	buffer.WriteString(dbPass)
	buffer.WriteByte('@')
	buffer.WriteString(dbAddress)
	buffer.WriteByte('/')
	buffer.WriteString(dbName)
	buffer.WriteString("?sslmode=disable")

	if errs, ok := migrate.UpSync(buffer.String(), migrationsPath); !ok {
		return fmt.Errorf("Database migrations failed: %v.", errs)
	}

	return nil
}
