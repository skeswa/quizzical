#!/bin/bash
sudo -u postgres psql -f /home/pentaho/pentaho-server-ce-7.0.0.0-25/pentaho-server/data/postgresql/create_quartz_postgresql.sql
sudo -u postgres psql -f /home/pentaho/pentaho-server-ce-7.0.0.0-25/pentaho-server/data/postgresql/create_jcr_postgresql.sql
sudo -u postgres psql -f /home/pentaho/pentaho-server-ce-7.0.0.0-25/pentaho-server/data/postgresql/create_repository_postgresql.sql
sudo -u postgres psql -f /home/pentaho/data/postgresql/create_pdirepo_postgresql.sql
