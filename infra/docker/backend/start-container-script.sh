#!/bin/sh
set -e
set -x
# This script will be executed once on a `docker run`.

# start Apache ACE client
java \
      -Dgosh.args=--nointeractive \
      -Dorg.osgi.service.http.port=${HTTP_PORT} \
      -Dosgi.shell.telnet.ip=${OSGI_SHELL_TELNET_IP} \
      -Dosgi.shell.telnet.port=${OSGI_SHELL_TELNET_PORT} \
      -Dosgi.shell.telnet.maxconn=${OSGI_SHELL_TELNET_MAXCONN} \
      -Dosgi.shell.telnet.socketTimeout=${OSGI_SHELL_TELNET_SOCKETTIMEOUT} \
      -jar gauntlet-backend.jar