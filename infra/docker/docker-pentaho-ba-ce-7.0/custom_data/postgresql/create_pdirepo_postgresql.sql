--
-- note: this script assumes pg_hba.conf is configured correctly
--

-- \connect postgres postgres

drop database if exists pdirepo;
drop user if exists q7ladmin;

CREATE USER q7ladmin PASSWORD 'q7ladmin';

CREATE DATABASE pdirepo WITH OWNER = q7ladmin ENCODING = 'UTF8' TABLESPACE = pg_default;

GRANT ALL PRIVILEGES ON DATABASE pdirepo to q7ladmin;
