FROM node:latest

# Set the environment variables accordingly.
ENV PORT="80"

# Mount the repository.
VOLUME ["/go/src/github.com/skeswa/gauntlet"]
WORKDIR /go/src/github.com/skeswa/gauntlet

# Expose port for HTTP.
EXPOSE 80

# Start the web server.
CMD cd ./web \
 && npm install --no-bin-links \
 && npm start
