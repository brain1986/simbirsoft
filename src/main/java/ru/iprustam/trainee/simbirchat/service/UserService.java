package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.repository.ChatUserRepository;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;

/**
 * Предоставляет сервис работы с пользователями
 */
@Service
public class UserService implements UserDetailsService {

    private final ChatUserRepository chatUserRepository;

    @Autowired
    public UserService(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ChatUser user = chatUserRepository.findByUsernameIgnoreCase(s);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public ChatUser findUser(String username) {
        return chatUserRepository.findByUsernameIgnoreCase(username);
    }

    public boolean isUserInRoom(ChatRoom chatRoom, Long userId) {
        return chatRoom.getUsers().stream().anyMatch(u -> u.getUserId() == userId);
    }

    public boolean checkAuthority(ChatUser chatUser, ChatAuthority chatAuthority) {
        return chatUser.getAuthorities().stream()
                .anyMatch(
                        a -> a.getAuthority().equals(chatAuthority.name())
                );
    }

    public void save(ChatUser chatUser) {
        chatUserRepository.save(chatUser);
    }
}
