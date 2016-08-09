package main

import (
	"fmt"
	"os"

	"gopkg.in/urfave/cli.v1"
)

const (
	environmentDev  = "dev"
	environmentProd = "prod"

	envVarsEnvironment = "GAUNTLET_ENV"
	envVarsPort        = "PORT, GAUNTLET_PORT"
	envVarsDbAddress   = "GAUNTLET_DB_ADDR"
	envVarsDbUser      = "GAUNTLET_DB_USER"
	envVarsDbPass      = "GAUNTLET_DB_PASS"
	envVarsDbName      = "GAUNTLET_DB_NAME"
	envVarsImagesPath  = "GAUNTLET_IMG_PATH"
)

// Config contains vital environment metadata used through out the backend.
type Config struct {
	IsDev      bool
	Port       int
	DbAddress  string
	DbUser     string
	DbPass     string
	DbName     string
	ImagesPath string
}

func (c *Config) String() string {
	return fmt.Sprintf(`Is dev:            %v
Port:              %d
Database address:  %s
Database name:     %s
Database user:     %s
Database password: %s
Images Path:       %s`, c.IsDev, c.Port, c.DbAddress, c.DbName, c.DbUser, c.DbPass, c.ImagesPath)
}

// GetConfig gets the configuration for the current execution environment.
func getConfig() *Config {
	var (
		environment string
		port        int
		dbAddress   string
		dbUser      string
		dbPass      string
		dbName      string
		imagesPath  string

		app            = cli.NewApp()
		actionExecuted = false
	)

	// Make the cli for config less boring.
	app.Usage = "the gauntlet api binary"

	// Map config variables 1:1 with flags.
	app.Flags = []cli.Flag{
		cli.StringFlag{
			Name:        "environment",
			Value:       environmentDev,
			Usage:       "execution context of this binary",
			EnvVar:      envVarsEnvironment,
			Destination: &environment,
		},
		cli.IntFlag{
			Name:        "port",
			Value:       3000,
			Usage:       "http port to exposed by this binary",
			EnvVar:      envVarsPort,
			Destination: &port,
		},
		cli.StringFlag{
			Name:        "db-address",
			Usage:       "address of the database",
			EnvVar:      envVarsDbAddress,
			Destination: &dbAddress,
		},
		cli.StringFlag{
			Name:        "db-user",
			Usage:       "user of the database",
			EnvVar:      envVarsDbUser,
			Destination: &dbUser,
		},
		cli.StringFlag{
			Name:        "db-pass",
			Usage:       "password of the database",
			EnvVar:      envVarsDbPass,
			Destination: &dbPass,
		},
		cli.StringFlag{
			Name:        "db-name",
			Usage:       "address of the database",
			EnvVar:      envVarsDbName,
			Destination: &dbName,
		},
		cli.StringFlag{
			Name:        "img-path",
			Usage:       "path to images",
			EnvVar:      envVarsImagesPath,
			Destination: &imagesPath,
		},
	}

	// Use the action to figure out whether the environment variables are accurate.
	app.Action = func(c *cli.Context) error {
		if environment != environmentDev && environment != environmentProd {
			return cli.NewExitError("invalid environment", 1)
		}
		if port < 1025 {
			return cli.NewExitError("invalid port", 2)
		}
		if len(dbAddress) < 1 {
			return cli.NewExitError("invalid db address", 3)
		}
		if len(dbUser) < 1 {
			return cli.NewExitError("invalid db user", 4)
		}
		if len(dbName) < 1 {
			return cli.NewExitError("invalid db name", 5)
		}
		if len(imagesPath) < 1 {
			return cli.NewExitError("invalid images path", 6)
		}

		actionExecuted = true
		return nil
	}

	// Execute the cli; wait to see what happens afterwards.
	app.Run(os.Args)

	// If there wasn't supposed to be an action, don't continue.
	if !actionExecuted {
		os.Exit(0)
	}

	return &Config{
		IsDev:      environment == environmentDev,
		Port:       port,
		DbAddress:  dbAddress,
		DbUser:     dbUser,
		DbPass:     dbPass,
		DbName:     dbName,
		ImagesPath: imagesPath,
	}
}
