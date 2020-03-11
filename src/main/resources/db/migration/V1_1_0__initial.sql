DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS room CASCADE;
DROP TABLE IF EXISTS usr CASCADE;
DROP TABLE IF EXISTS message CASCADE;
DROP TABLE IF EXISTS room_user CASCADE;


CREATE TABLE role (
    role_id SMALLSERIAL NOT NULL,
    authorities smallint DEFAULT 0,
    role_name character varying(255)
);

CREATE TABLE message (
    message_id BIGSERIAL NOT NULL,
    message character varying(255),
    message_time timestamp without time zone,
    room_id bigint,
    user_id bigint NOT NULL
);

CREATE TABLE room (
    room_id BIGSERIAL NOT NULL,
    owner_user_id bigint NOT NULL,
    room_name character varying(255),
    room_type integer
);

CREATE TABLE room_user (
    room_id bigint NOT NULL,
    user_id bigint NOT NULL
);

CREATE TABLE usr (
    user_id BIGSERIAL NOT NULL,
    blocked boolean NOT NULL,
    password character varying(255),
    username character varying(255),
    role_id smallint NOT NULL
);


ALTER TABLE ONLY message
    ADD CONSTRAINT message_pkey PRIMARY KEY (message_id);

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);

ALTER TABLE ONLY room
    ADD CONSTRAINT room_pkey PRIMARY KEY (room_id);
    
ALTER TABLE ONLY room_user
    ADD CONSTRAINT room_user_pkey PRIMARY KEY (room_id, user_id);
    
ALTER TABLE ONLY usr
    ADD CONSTRAINT usr_pkey PRIMARY KEY (user_id);

ALTER TABLE ONLY room
    ADD CONSTRAINT fk96asdasdcaweeewussdkfjkds FOREIGN KEY (owner_user_id) REFERENCES usr(user_id);

ALTER TABLE ONLY room_user
    ADD CONSTRAINT fk3cyu6d3buptgjo9g8qcvhothn FOREIGN KEY (user_id) REFERENCES usr(user_id);

ALTER TABLE ONLY message
    ADD CONSTRAINT fk70bv6o4exfe3fbrho7nuotopf FOREIGN KEY (user_id) REFERENCES usr(user_id);

ALTER TABLE ONLY usr
    ADD CONSTRAINT fk96p7c54b66irtx3yusoitsiva FOREIGN KEY (role_id) REFERENCES role(role_id);

ALTER TABLE ONLY message
    ADD CONSTRAINT fkl1kg5a2471cv6pkew0gdgjrmo FOREIGN KEY (room_id) REFERENCES room(room_id);

ALTER TABLE ONLY room_user
    ADD CONSTRAINT fktakjqllocgakgw0os4hygxfk1 FOREIGN KEY (room_id) REFERENCES room(room_id);


INSERT INTO role (role_id, role_name, authorities) VALUES
  (1, 'ROLE_ADMIN', 4095), 
  (2, 'ROLE_MODERATOR', 1011), 
  (3, 'ROLE_USER', 944),
  (4, 'ROLE_BOT', 4095),
  (5, 'ROLE_BLOCKED', 32);

INSERT INTO usr (user_id, password, username, role_id, blocked) VALUES
  (1, '$2a$10$hn03kGjb2fJ6Hc2Zq9JIn.JENytzfs9zlomioihi968O6Sx0.UtES', 'user1', 3, false), 
  (2, '$2a$10$CPtrB9BOnjo0ePyIodCfmuEubmbdvrYH69wP/bC0DhEaXgAAGJ3/G', 'user2', 3, false),
  (3, '$2a$10$nkO.V6bnpKIX0tm73hn.iOJvUJi0xr.UU4AFpn/JNFSJGuhUR/xgC', 'admin', 1, false),
  (4, '$2a$10$gTq2bMoapI8psU75H9c1.OUOOAc9zeMEBgwE3Wz5OGqnQcgRBlu/S', 'moder', 2, false),
  (5, '$2a$10$Ai6Zt3J080o4BoUzjOgF/uJzaDwT/EODsOX./2aRe4CsQZh5ixvQy', 'bot', 4, false);

INSERT INTO room (room_id, owner_user_id, room_type, room_name) VALUES
(1, 3, 0, 'Общая комната'),
(2, 1, 1, 'Комната 2'),
(3, 1, 2, 'Комната 3'),
(4, 2, 3, 'Комната 4');
  
INSERT INTO message (message, message_time, user_id, room_id) VALUES
  ('Hello from user1', '2020-03-03T15:30:00+03:00', 1, 1), 
  ('Hello from user1', '2020-03-03T20:32:00+03:00', 1, 1),
  ('Hello from user2', '2020-03-03T20:33:00+03:00', 2, 1),
  ('Hello from user2', '2020-03-03T20:34:00+03:00', 2, 1),
  ('Hello from user3', '2020-03-03T20:35:00+03:00', 3, 2),
  ('Hello from user3', '2020-03-03T20:35:00+03:00', 3, 2);      
  
INSERT INTO room_user (room_id, user_id) VALUES
  (1, 1), 
  (1, 2),  
  (1, 3), 
  (2, 1),
  (2, 3),
  (3, 1),
  (3, 4);

