#!/bin/sh
set -e
set -x
# This script will be executed once on a `docker run`.

# start Apache ACE client
java \
      -Dgosh.args=--nointeractive \
      -jar quizzical-importer.jar