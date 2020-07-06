mvn clean package
sudo systemctl stop tomcat
cp ~/matcha/spark-server/target/spark-server-1.0.war /opt/tomcat/webapps/spark-server-1.0.war
sudo sh /opt/tomcat/bin/startup.sh
sudo systemctl start tomcat
