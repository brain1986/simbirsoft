Для запуска требуется JDK 11

после запуска использовать URL:
localhost:8080/login

""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

Схема БД:
https://dbdesigner.page.link/AwfVBy1yfAadXZ6F8

Дамп БД (для flyway):
db/migration/V1_1_0__initial.sql

Конфигурация flyway содержится в файле pom.xml в блоке plugins

Миграция выполняется с помощью команды
mvn clean flyway:migrate

""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

Управление матрицей действий для каждой роли пользователя делается в таблице role в поле authorities. 
Каждый бит, в зависимости от значения управляет следующими действиями:

бит# действие
0:   USER_BLOCKING
1    USER_UNBLOCKING
2    MODERATOR_SET 
3    MODERATOR_DELETE
4    MESSAGE_SEND
5    MESSAGE_RECEIVE
6    MESSAGE_DELETE
7    PUBLIC_ROOM_CREATE
8    PRIVATE_ROOM_CREATE
9    USER_ADD
10   USER_DELETE
11   ROOM_RENAME

Примеры
Простой пользователь
b001110110000=944

Модератор
b001111110011=1011

Администратор
b111111111111=4095

Заблокированный
b000000100000=32