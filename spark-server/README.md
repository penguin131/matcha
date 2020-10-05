<b>For start:</b><br>

1)run docker-compose up -d for create database <br>
2)Configure application.properties database url <br>
3)run mvn clean package <br>
4)run mvn exec:java <br>
<br><br><br>
API https://app.swaggerhub.com/apis/smight/matcha-server/1.0.0#<br>
<br>
todo:<br>
пофиксить oauth2(поменять логику создания нового юзера)
перепроверить развертывание всего с нуля
залить все в мейн ветку, когда будет готов фронт
перепроверить генерацию скульных запросов
убрать NPE при OAUTH2 когда токен устаревший
Закинуть все в докер


че тестить:
всегда проверять ответы от сервера
редирект без авторизации
авторизация двумя способами
один и тот же чат от одного юзера


