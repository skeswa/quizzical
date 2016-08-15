# gauntlet
A web app that makes test prep easy.

## Pre-requisites
- Development environment has only been tested on macOS
- Docker must be installed in the development environment (no `docker-machine`)

## Setup
Clone the repository into `$GOPATH/src/github.com/skeswa/gauntlet`. Then naviagte into the repository in your terminal, and execute:
```shell
./infra/scripts/build-images.sh
```
Afterwards, execute:
```shell
./infra/scripts/start-containers.sh
```
Then, in your favorite browser, open [http://localhost/](http://localhost/). At this point, you should be able to see the gauntlet interface.  
  
Tada! The `gauntlet` development environment should be ready for you. Each of the running containers will automagically update when you change the filesystem until you run:
```shell
./infra/scripts/stop-containers.sh
```
## Dockerizing
gauntlet git:(master) docker-compose -f ./infra/docker-compose.yml build 
gauntlet git:(master) ✗ docker-compose -f ./infra/docker-compose.yml down
docker-compose -f ./infra/docker-compose.yml up -d
## REST notes
See /Users/mduduzikeswa/Documents/dev/mazasoft/go/src/github.com/skeswa/gauntlet/api/main.go for routes/endpoints
See /Users/mduduzikeswa/Documents/dev/mazasoft/go/src/github.com/skeswa/gauntlet/api/dto for JSOn structure
