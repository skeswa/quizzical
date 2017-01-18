set -e

# Apply env vars
sed -i "s/QUIZZICAL_DB_USERNAME/${QUIZZICAL_DB_USERNAME}/g" conf/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg
sed -i "s/QUIZZICAL_DB_PASS/${QUIZZICAL_DB_PASS}/g" conf/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg
sed -i "s/QUIZZICAL_DB_HOST/${QUIZZICAL_DB_HOST}/g" conf/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg
sed -i "s/QUIZZICAL_DB_PORT/${QUIZZICAL_DB_PORT}/g" conf/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg
sed -i "s/QUIZZICAL_DB_NAME/${QUIZZICAL_DB_NAME}/g" conf/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg
sed -i "s/QUIZZICAL_DB_SCHEMA/${QUIZZICAL_DB_SCHEMA}/g" conf/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg


sed -i "s/QUIZZICAL_DB_USERNAME/${QUIZZICAL_DB_USERNAME}/g" conf/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg
sed -i "s/QUIZZICAL_DB_PASS/${QUIZZICAL_DB_PASS}/g" conf/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg
sed -i "s/QUIZZICAL_DB_HOST/${QUIZZICAL_DB_HOST}/g" conf/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg
sed -i "s/QUIZZICAL_DB_PORT/${QUIZZICAL_DB_PORT}/g" conf/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg
sed -i "s/QUIZZICAL_DB_NAME/${QUIZZICAL_DB_NAME}/g" conf/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg
sed -i "s/QUIZZICAL_DB_SCHEMA/${QUIZZICAL_DB_SCHEMA}/g" conf/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg

sed -i "s/OSGI_SHELL_TELNET_IP/${OSGI_SHELL_TELNET_IP}/g" start_gauntlet_backend.sh
sed -i "s/OSGI_SHELL_TELNET_PORT/${OSGI_SHELL_TELNET_PORT}/g" start_gauntlet_backend.sh
sed -i "s/OSGI_SHELL_TELNET_MAXCONN/${OSGI_SHELL_TELNET_MAXCONN}/g" start_gauntlet_backend.sh
sed -i "s/OSGI_SERVICE_HTTP_PORT/${OSGI_SERVICE_HTTP_PORT}/g" start_gauntlet_backend.sh

# start client
sh start_gauntlet_backend.sh
