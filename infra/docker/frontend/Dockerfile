FROM jeanblanchard/busybox-java:8

MAINTAINER jeanblanchard

# Set env
ENV JCR_CLIENT_JAVA_OPTS "-Xms512m -Xmx1024m -XX:MaxMetaspaceSize=512m"
# install ace target files
COPY distributions/quizzical-frontend.jar /root/quizzical-frontend.jar
COPY configuration /root/load

##################################################################################################
## START CUSTOMIZATION

# Create a mount point
VOLUME ["/root/backups"]

# Expose ports
#EXPOSE 8080
#EXPOSE 9999

# Add the Apache Ace start script (executed on a `docker run`).
ADD init-container-script.sh /root/init_quizzical_frontend.sh
RUN chmod +x /root/init_quizzical_frontend.sh

# Add the Apache Ace start script (executed on a `docker run`).
ADD start-container-script.sh /root/start_quizzical_frontend.sh
RUN chmod +x /root/start_quizzical_frontend.sh

WORKDIR /root

# Use baseimage-docker's init system.
CMD ["sh","init_quizzical_frontend.sh"]

