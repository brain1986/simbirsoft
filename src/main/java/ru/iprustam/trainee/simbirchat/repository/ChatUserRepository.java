package ru.iprustam.trainee.simbirchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    ChatUser findByUsernameIgnoreCase(String username);
}
