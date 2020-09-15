<b>For start:</b><br>

1)install two postgresql databases for prod and test <br>
2)Configure test/resources/application.properties, src/main/resources/application.properties and src/main/resources/META-INF/persistence.xml <br>
3)run src/main/resources/database_schema/wsg84_check_distance.sql in other databases <br>
4)run mvn clean package <br>
5)run mvn exec:java <br>
<br><br><br>
API https://app.swaggerhub.com/apis/smight/matcha-server/1.0.0#<br>
<br>
todo:<br>
бонусы, которые можно сделать: OAUTH2, Google+ or another clouds image import


//todo дублирование лайка, возвращение лайка и дазлайка в списке юзеров
короче, надо нормально оттестить setLike6 setComplaint, getAllFriendsForLogin



Доделал:
1)дублирование проставления лайка и дизлайка, тесты на них
2)Регистрация юзера. Хз почему старый рандомайзер зависал на сервере.


55.912206, 49.385289
55.702021, 48.789298
