set -e

sed -i "s/OSGI_SHELL_TELNET_IP/${OSGI_SHELL_TELNET_IP}/g" start_quizical_importer.sh
sed -i "s/OSGI_SHELL_TELNET_PORT/${OSGI_SHELL_TELNET_PORT}/g" start_quizical_importer.sh
sed -i "s/OSGI_SHELL_TELNET_MAXCONN/${OSGI_SHELL_TELNET_MAXCONN}/g" start_quizical_importer.sh
sed -i "s/OSGI_SERVICE_HTTP_PORT/${OSGI_SERVICE_HTTP_PORT}/g" start_quizical_importer.sh

# start client
sh start_quizical_importer.sh