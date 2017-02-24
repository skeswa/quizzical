FROM postgres:latest

# COPY local scripts
COPY docker-entrypoint-initdb.d/*.* /docker-entrypoint-initdb.d
#RUN chmod +x /docker-entrypoint-initdb.d/*.sh

ENV POSTGRES_DB="quizzical"
ENV POSTGRES_USER="quizzical-admin"
ENV POSTGRES_PASSWORD="getmandisagoodatsatmath"
