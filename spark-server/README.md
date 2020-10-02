<b>For start:</b><br>

1)run docker-compose up -d for create database <br>
2)Configure application.properties database url <br>
3)run mvn clean package <br>
4)run mvn exec:java <br>
<br><br><br>
API https://app.swaggerhub.com/apis/smight/matcha-server/1.0.0#<br>
<br>
todo:<br>
бонусы, которые можно сделать: OAUTH2, Google+ or another clouds image import


oauth2
1)Получить id и secret
2)Получить токен доступа
3)Получить инфу о юзере, логин итд
4)Создать нового юзера, если нужно
5)Выдать свой токен



1)С фронта должна идти ссылка https://api.intra.42.fr/oauth/authorize?client_id=7ff75fdfa415c5709f7d9257bc163dbd22654eae9a10799daffeb52026b924ac&redirect_uri=http%3A%2F%2Flocalhost%3A8080&response_type=code
2)По ссылке тыкаешь далее или логинишься в интре, редиректит на указанный в урле выше адрес(потом поменяю на какой скажешь). В конце будет код доступа. Пример: http://localhost:8080/?code=3a5e0fa7ca4235c2a4ce940af7a200dd03b8f5ffc9e7e67e2b2b7aedf80107ec
3)Его послать в бэк, в getToken. Добавлю новое поле для него туды. В ответ придет уже привычный jwt токен



3)Надо идти за токеном  https://api.intra.42.fr/oauth/token?client_id=7ff75fdfa415c5709f7d9257bc163dbd22654eae9a10799daffeb52026b924ac&redirect_uri=http%3A%2F%2Flocalhost%3A8080&response_type=code
4)С токеном идти на сервер
