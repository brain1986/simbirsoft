package ru.iprustam.trainee.simbirchat.repos;

import ru.iprustam.trainee.simbirchat.user.ChatUser;

import java.util.List;

public interface UserRepository {
    void addUser(ChatUser user);
    void deleteUser(int userId);
    ChatUser findUserById(int userId);
    List<ChatUser> findAllUsers();
}
