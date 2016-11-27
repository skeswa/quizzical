#! /bin/sh

# Make sure that we're setup with the docker machine correctly
eval $(docker-machine env default)

# Build
docker build -t conxadmin/gauntlet-backend .
docker push conxadmin/gauntlet-backend