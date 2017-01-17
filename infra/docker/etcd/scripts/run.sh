#! /bin/sh

# Make sure that we're setup with the docker machine correctly
eval $(docker-machine env default)

# Run the pentaho-postgres container first
read MY_SSH_KEY < ~/.ssh/id_rsa.pub
docker run -d \
  -p 2223:22 \
  -p 5433:5432 \
  --name pentaho-postgresql \
  --volume=/Users/mduduzikeswa/Documents/dev/projects/pentaho/docker-pentaho-postgres9x/data:/srv/pgdata \
  -e "PG_USERNAME=pentahoadmin" \
  -e "PG_PASSWORD=pentahoadmin!" \
  -e "SSH_PUBLIC_KEY=$MY_SSH_KEY" \
  mduduzik/docker-pentaho-postgres9x