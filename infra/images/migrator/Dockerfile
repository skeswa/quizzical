FROM golang:alpine

# Install git for dependencies
RUN set -ex && apk add --update 'git' && apk add 'bash'

# Build source and move things around.
COPY . /go/src/github.com/skeswa/gauntlet
RUN cd /go/src/github.com/skeswa/gauntlet/migrator \
    && go get -d -v \
    && go build -o migrator \
    && chmod +x ./migrator \
    && mkdir /gauntlet \
    && mv ./migrator /gauntlet/migrator \
    && mv ../infra/scripts/wait-for-it.sh /gauntlet/wait-for-it.sh \
    && chmod +x /gauntlet/wait-for-it.sh \
    && cd /gauntlet \
    && rm -rf /go

# Uninstall unnecessary tools.
RUN set -ex && apk del git

# Set the environment variables
ENV GAUNTLET_DB_ADDR="db"
ENV GAUNTLET_DB_NAME="gauntlet"
ENV GAUNTLET_DB_USER="gauntlet-admin"
ENV GAUNTLET_DB_PASS="getmandisagoodatsatmath"
ENV GAUNTLET_MIGRATIONS_PATH="/gauntlet/migrations"

# Mount the migrations.
VOLUME ["/gauntlet/migrations"]

# Run the migrator.
WORKDIR /gauntlet
CMD ./wait-for-it.sh -h db -p 5432 -t 0 -- ./migrator
