set -e

# Apply env vars
sed -i "s/GAUNTLET_DB_USERNAME/${GAUNTLET_DB_USERNAME}/g" load/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg
sed -i "s/GAUNTLET_DB_PASS/${GAUNTLET_DB_PASS}/g" load/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg
sed -i "s/GAUNTLET_DB_HOST/${GAUNTLET_DB_HOST}/g" load/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg
sed -i "s/GAUNTLET_DB_PORT/${GAUNTLET_DB_PORT}/g" load/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg
sed -i "s/GAUNTLET_DB_NAME/${GAUNTLET_DB_NAME}/g" load/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg
sed -i "s/GAUNTLET_DB_SCHEMA/${GAUNTLET_DB_SCHEMA}/g" load/org.amdatu.jpa.datasourcefactory-jta-gauntlet.cfg


sed -i "s/GAUNTLET_DB_USERNAME/${GAUNTLET_DB_USERNAME}/g" load/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg
sed -i "s/GAUNTLET_DB_PASS/${GAUNTLET_DB_PASS}/g" load/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg
sed -i "s/GAUNTLET_DB_HOST/${GAUNTLET_DB_HOST}/g" load/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg
sed -i "s/GAUNTLET_DB_PORT/${GAUNTLET_DB_PORT}/g" load/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg
sed -i "s/GAUNTLET_DB_NAME/${GAUNTLET_DB_NAME}/g" load/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg
sed -i "s/GAUNTLET_DB_SCHEMA/${GAUNTLET_DB_SCHEMA}/g" load/org.amdatu.jpa.datasourcefactory-nonjta-gauntlet.cfg


# start client
sh start_gauntlet_backend.sh