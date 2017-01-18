FROM jeanblanchard/busybox-java:8

MAINTAINER jeanblanchard

# Set env
ENV JCR_CLIENT_JAVA_OPTS "-Xms512m -Xmx1024m -XX:MaxMetaspaceSize=512m"
# install ace target files
COPY distributions/quizzical-backend.jar /root/quizzical-backend.jar
COPY configuration /root/conf

##################################################################################################
## START CUSTOMIZATION

# Create a mount point
VOLUME ["/root/backups"]

# Expose ports
#EXPOSE 8080
#EXPOSE 9999

# Add the Apache Ace start script (executed on a `docker run`).
ADD init-container-script.sh /root/init_gauntlet_backend.sh
RUN chmod +x /root/init_gauntlet_backend.sh

# Add the Apache Ace start script (executed on a `docker run`).
ADD start-container-script.sh /root/start_gauntlet_backend.sh
RUN chmod +x /root/start_gauntlet_backend.sh

WORKDIR /root

# Use baseimage-docker's init system.
CMD ["sh","init_gauntlet_backend.sh"]
