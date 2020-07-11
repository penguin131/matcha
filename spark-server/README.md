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

chat deploy:
mvn clean install
mvn exec:java

Postgres Database:<br>
User sammy, password 123<br>
Enter to psql command line: sudo -u sammy psql<br>
Remote connection: psql -h 84.38.183.163 -d sammy -U sammy<br>

Клиент: https://stackoverflow.com/questions/32586990/how-to-send-http-request-using-jwt-token-for-authentication-from-cookie-storage
Короче, суть в чем: у клиента хранится хеш токена. Он пихает его в каждый свой заголовок запроса к серверу.
При авторизации клиент отдельно запрашивает свой токен. У токена есть срок жизни, сервер сам должен проверять его и если что обновлять.

Нужны еще таблички:
Теги
Справочник тегов
Картинки
Возможность проверять кто проверял и кто лайкнул
Рейтинг
Отбор анкет по фильтрам
Различные сортировки
Чат
Кнопка пожаловаться
Уведомления


GPS - only frontend?

{"login":"smight","password":"smight", "email":"zininlol@rambler.ru", "sex":0}

Сейчас сложность: чатик и уведомления, короче работа через сокеты
