��� ������� ��������� JDK 11

����� ������� ������������ URL:
localhost:8080/login

""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

����� ��:
https://dbdesigner.page.link/AwfVBy1yfAadXZ6F8

���� �� (��� flyway):
db/migration/V1_1_0__initial.sql

������������ flyway ���������� � ����� pom.xml � ����� plugins

�������� ����������� � ������� �������
mvn clean flyway:migrate

""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

���������� �������� �������� ��� ������ ���� ������������ �������� � ������� role � ���� authorities. 
������ ���, � ����������� �� �������� ��������� ���������� ����������:

���# ��������
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

�������
������� ������������
b001110110000=944

���������
b001111110011=1011

�������������
b111111111111=4095

���������������
b000000100000=32