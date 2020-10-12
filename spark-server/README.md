<b>For start:</b><br>

1)run "docker-compose up -d" for create database(with sudo on ubuntu) <br>
2)Configure application.properties database url <br>
3)run mvn clean package <br>
4)run mvn exec:java <br>
<br><br><br>
API https://app.swaggerhub.com/apis/smight/matcha-server/1.0.0#<br>
<br>
todo:<br>


GUIDE:
1)Clone from intra
BACKEND:
    2)copy server application to remote server: scp -r ~/Desktop/matcha-intra/server smight@84.201.139.26:matcha-intra
    3)connect to server: ssh smight@84.201.139.26
    4)go to /matcha-intra and copy database dump with photos cp ../init_database_with_data.sql init_database.sql
    5)run database into docker: sudo docker-compose up -d
    6)wait base init and start mvn exec:java
FRONTEND:
    7)yarn install
    8)yarn build 
    9)yarn global add serve
    10)serve -s build







