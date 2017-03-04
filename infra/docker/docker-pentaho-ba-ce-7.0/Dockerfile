FROM mazagroup/pentaho-base

MAINTAINER Rowell Belen developer@bytekast.com

# Install useful command line utilities
RUN apt-get update && apt-get install -y curl
RUN apt-get update && apt-get install -y man
RUN apt-get update && apt-get install -y zip unzip

# Postgres AUFS bug hack
RUN mkdir /etc/ssl/private-copy; mv /etc/ssl/private/* /etc/ssl/private-copy/; rm -r /etc/ssl/private; mv /etc/ssl/private-copy /etc/ssl/private; chmod -R 0700 /etc/ssl/private; chown -R postgres /etc/ssl/private

# Download and extract pentaho BA Server binary
WORKDIR /home/pentaho/
RUN wget https://sourceforge.net/projects/pentaho/files/Business%20Intelligence%20Server/7.0/pentaho-server-ce-7.0.0.0-25.zip
RUN unzip pentaho-server-ce-7.0.0.0-25.zip -d pentaho-server-ce-7.0.0.0-25
RUN rm pentaho-server-ce-7.0.0.0-25.zip

# Copy config files
ADD config.sh /home/pentaho/
RUN chmod +x /home/pentaho/config.sh
ADD config/tomcat/context.xml pentaho-server-ce-7.0.0.0-25/pentaho-server/tomcat/webapps/pentaho/META-INF/

# Copy PDI repo db scripts
ADD custom_data/postgresql/create_pdirepo_postgresql.sql /home/pentaho/data/postgresql/create_pdirepo_postgresql.sql

# Add/run script to load default tables
ADD loaddb.sh /home/pentaho/
RUN chmod +x /home/pentaho/loaddb.sh
RUN /etc/init.d/postgresql start && \
	printf 'password\n' | /home/pentaho/loaddb.sh

# Add script to start biserver
ADD start.sh /home/pentaho/
RUN chmod +x /home/pentaho/start.sh

# Redirect Tomcat output
ENV CATALINA_OUT /dev/stdout

# Start Service
EXPOSE 8080 22 5432
CMD /home/pentaho/start.sh && /usr/bin/supervisord
