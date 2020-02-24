package ru.iprustam.trainee.simbirchat.repos;

import org.springframework.stereotype.Component;
import ru.iprustam.trainee.simbirchat.user.ChatUser;

import java.util.List;

@Component
public class ChatUserRepo implements ChatUserRepository {


    @Override
    public void addUser(ChatUser user) {

    }

    @Override
    public void deleteUser(Integer userId) {

    }

    @Override
    public ChatUser findUserById(Integer userId) {
        return null;
    }

    @Override
    public List<ChatUser> findAllUsers() {
        return null;
    }
}
