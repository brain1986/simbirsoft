
Для запуска требуется JDK 11<br />

после запуска использовать URL:<br />
http://localhost:8080/login<br />


""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
#### демо видео для v2: ####
https://cloud.mail.ru/public/6JHE/48ffxAwij<br />

#### демо видео для v3: ####
https://cloud.mail.ru/public/SEzV/4hpQ7KufS<br />

""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
#### Схема БД ####
https://dbdesigner.page.link/ZGqsA9AumKJcasfH9<br />

Дамп БД (для flyway):<br />
*src/main/resources/db/migration/V1_1_0__initial.sql*<br />

""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

Логины | пароли
-------|-------
user1  | user1
user2  | user2
user3  | user3
admin  | admin
moder  | moder


""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

#### Authorities ####

Управление матрицей действий для каждой роли пользователя делается в таблице role в поле authorities. <br />
Каждый бит, в зависимости от значения управляет следующими действиями:<br />

бит# | действие
-----|----------------
0:   | USER_BAN
1    | USER_UNBAN
2    | MODERATOR_SET
3    | MODERATOR_DELETE
4    | MESSAGE_SEND
5    | MESSAGE_RECEIVE
6    | MESSAGE_DELETE
7    | PUBLIC_ROOM_CREATE
8    | PRIVATE_ROOM_CREATE
9    | ROOM_CONNECT
10   | USER_DELETE
11   | ROOM_RENAME
12   | ROOM_REMOVE
13   | USER_RENAME

#### Примеры ####
Простой пользователь<br />
b11111110110000=16304<br />

Модератор<br />
b11111111110011=16371<br />

Администратор<br />
b11111111111111=16383<br />

Заблокированный<br />
b000000000000=0<br />
