INSERT INTO room (room_id) VALUES
  (1), 
  (2);
  
INSERT INTO usr (user_id, password, username, role_id) VALUES
  (1, 'passs', 'user1', 1), 
  (2, 'passs', 'user2', 1),
  (3, 'passs', 'user3', 3),
  (4, 'passs', 'user4', 3);  
  
INSERT INTO message (message_id, message, user_id, room_id) VALUES
  (1, 'from user1', 1, 1), 
  (2, 'from user1', 1, 1),
  (3, 'from user2', 2, 1),
  (4, 'from user2', 2, 1),
  (5, 'from user3', 3, 2),
  (6, 'from user3', 3, 2);    
  
INSERT INTO room_user (room_id, user_id) VALUES
  (1, 1), 
  (1, 2),  
  (2, 1),
  (2, 3);
  