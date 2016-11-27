FROM golang:alpine

# Install git for dependencies
RUN set -ex && apk add --update 'git' && apk add 'bash'

# Build source and move things around.
COPY . /go/src/github.com/skeswa/gauntlet
RUN echo "Installing dependencies..." \
   && cd /go/src/github.com/skeswa/gauntlet/api && go get -d -v
RUN echo "Building the binary..." \
   && cd /go/src/github.com/skeswa/gauntlet/api \
   && go build -o api \
   && chmod +x ./api \
   && mkdir /gauntlet \
   && mv ./api /gauntlet/api \
   && mv ../infra/scripts/wait-for-it.sh /gauntlet/wait-for-it.sh \
   && chmod +x /gauntlet/wait-for-it.sh \
   && cd /gauntlet \
   && rm -rf /go

# Uninstall unnecessary tools.
RUN set -ex && apk del git

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

# Expose the right port.
EXPOSE 3000

# Run the migrator.
WORKDIR /gauntlet
CMD ./wait-for-it.sh -h db -p 5432 -t 0 -- ./api --port 3000
