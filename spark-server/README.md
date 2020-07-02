Service for working with the base
API: https://app.swaggerhub.com/apis/smight/matcha-server/1.0.0#

Deploy guide:
1. Clone this repository to your local machine
2. Run mvn package
3. Copy the generated `spark-server.war` to the Tomcat `webapps` folder and run `startup.sh`:
    3.1 sudo systemctl stop tomcat
    3.2 cp ~/matcha/spark-server/target/spark-server-1.0.war /opt/tomcat/webapps/spark-server-1.0.war
    3.3 sudo sh /opt/tomcat/bin/startup.sh
    3.4 sudo systemctl start tomcat

Postgres Database:<br>
User sammy, password 123<br>
Enter to psql command line: sudo -u sammy psql<br>
Remote connection: psql -h 84.38.183.163 -d sammy -U sammy<br>
