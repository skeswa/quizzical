set -e

sed -i "s/OSGI_SHELL_TELNET_IP/${OSGI_SHELL_TELNET_IP}/g" start_quizzical_frontend.sh
sed -i "s/OSGI_SHELL_TELNET_PORT/${OSGI_SHELL_TELNET_PORT}/g" start_quizzical_frontend.sh
sed -i "s/OSGI_SHELL_TELNET_MAXCONN/${OSGI_SHELL_TELNET_MAXCONN}/g" start_quizzical_frontend.sh
sed -i "s/OSGI_SERVICE_HTTP_PORT/${OSGI_SERVICE_HTTP_PORT}/g" start_quizzical_frontend.sh

# start client
sh start_quizzical_frontend.sh