#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE q7ledw;
    GRANT ALL PRIVILEGES ON DATABASE q7ledw TO $POSTGRES_USER;
EOSQL