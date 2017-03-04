set -e
sed -i "s/Q7L_EDW_DB_USERNAME/$Q7L_EDW_DB_USERNAME/g" pentaho-server-ce-7.0.0.0-25/pentaho-server/tomcat/webapps/pentaho/META-INF/context.xml
sed -i "s/Q7L_EDW_DB_PASS/$Q7L_EDW_DB_PASS/g" pentaho-server-ce-7.0.0.0-25/pentaho-server/tomcat/webapps/pentaho/META-INF/context.xml
sed -i "s/Q7L_EDW_DB_HOST/$Q7L_EDW_DB_HOST/g" pentaho-server-ce-7.0.0.0-25/pentaho-server/tomcat/webapps/pentaho/META-INF/context.xml
sed -i "s/Q7L_EDW_DB_PORT/$Q7L_EDW_DB_PORT/g" pentaho-server-ce-7.0.0.0-25/pentaho-server/tomcat/webapps/pentaho/META-INF/context.xml
sed -i "s/Q7L_EDW_DB_NAME/$Q7L_EDW_DB_NAME/g" pentaho-server-ce-7.0.0.0-25/pentaho-server/tomcat/webapps/pentaho/META-INF/context.xml
