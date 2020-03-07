package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.repository.ChatUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Предоставляет сервис работы с пользователями
 */
@Service
public class UserService implements UserDetailsService {

    private final ChatUserRepository chatUserRepository;
    private final SimpUserRegistry simpUserRegistry;

    @Autowired
    public UserService(ChatUserRepository chatUserRepository, SimpUserRegistry simpUserRegistry) {
        this.chatUserRepository = chatUserRepository;
        this.simpUserRegistry = simpUserRegistry;
    }

    public static UserDetails getLoggedUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ChatUser user = chatUserRepository.findByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public Optional<ChatUser> authenticate(String username, String passwordHash) {
        Optional<ChatUser> chatUser = Optional.ofNullable(chatUserRepository.findByUsernameAndPassword(username, passwordHash));
        return chatUser;
    }

    public void getActiveWebsocketUsers() {
        List list = simpUserRegistry
                .getUsers()
                .stream()
                .map(SimpUser::getName)
                .collect(Collectors.toList());
    }

    public List<ChatUser> findUsers(Long roomId) {
        List<ChatUser> users = chatUserRepository.findByRoomId(roomId);
        return users;
    }

}
