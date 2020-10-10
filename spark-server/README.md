<b>For start:</b><br>

1)run "docker-compose up -d" for create database(with sudo on ubuntu) <br>
2)Configure application.properties database url <br>
3)run mvn clean package <br>
4)run mvn exec:java <br>
<br><br><br>
API https://app.swaggerhub.com/apis/smight/matcha-server/1.0.0#<br>
<br>
todo:<br>

залить все в мейн ветку, когда будет готов фронт


че тестить:
всегда проверять ответы от сервера(>=500 не должно быть)
редирект без авторизации
авторизация двумя способами
смена пароля при разных способах авторизации
смена почты 
биография, картинки, теги, предпочтения

один и тот же чат от одного юзера
валидация заполняемых из форм полей
если нет токена в локалстораге, могу зайти в поиск. Короче роутинг
потестить поиск друзей с фильтром и прочее



//сегодня
потестить правки на фронте

статус онлайна в профиле+
банан++
фильтр на пол по умолчанию++
не подтвержденная учетка++
A user that doesn't have a profile picture can't like another user.++
задать координаты++


перебивка лайка и дизлайка
