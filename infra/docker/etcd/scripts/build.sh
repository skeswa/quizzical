#! /bin/sh

# Make sure that we're setup with the docker machine correctly
eval $(docker-machine env default)

# Build
docker build -t mazagroup/etcd .
docker push mazagroup/etcd
