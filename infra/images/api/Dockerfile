FROM golang:alpine

# Install sparkplug.
RUN set -ex && apk add --update 'git'
RUN go get -v github.com/skeswa/sparkplug

# Set the environment variables accordingly.
ENV PORT="3000"
ENV GAUNTLET_ENV="dev"
ENV GAUNTLET_DB_ADDR="db"
ENV GAUNTLET_DB_NAME="gauntlet"
ENV GAUNTLET_DB_USER="gauntlet-admin"
ENV GAUNTLET_DB_PASS="getmandisagoodatsatmath"
ENV GAUNTLET_IMG_PATH="/gauntlet/images"

# Mount the repository.
VOLUME ["/go/src/github.com/skeswa/gauntlet", "/gauntlet/images"]
WORKDIR /go/src/github.com/skeswa/gauntlet

# Expose port for HTTP.
EXPOSE 3000

# Start the api.
CMD cd ./api \
 && go get -d -v \
 && sparkplug
