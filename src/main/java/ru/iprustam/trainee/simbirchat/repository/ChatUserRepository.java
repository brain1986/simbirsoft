package ru.iprustam.trainee.simbirchat.repos;

import ru.iprustam.trainee.simbirchat.user.ChatUser;

import java.util.List;

public interface ChatUserRepository {
    void addUser(ChatUser user);
    void deleteUser(Integer userId);
    ChatUser findUserById(Integer userId);
    List<ChatUser> findAllUsers();
}
