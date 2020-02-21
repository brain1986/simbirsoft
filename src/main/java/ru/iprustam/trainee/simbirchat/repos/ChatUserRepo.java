package ru.iprustam.trainee.simbirchat.repos;

import org.springframework.stereotype.Component;
import ru.iprustam.trainee.simbirchat.user.ChatUser;

import java.util.List;

@Component
public class UserRepo implements UserRepository {
    @Override
    public void addUser(ChatUser user) {
        
    }

    @Override
    public void deleteUser(int userId) {

    }

    @Override
    public ChatUser findUserById(int userId) {
        return null;
    }

    @Override
    public List<ChatUser> findAllUsers() {
        return null;
    }
}
