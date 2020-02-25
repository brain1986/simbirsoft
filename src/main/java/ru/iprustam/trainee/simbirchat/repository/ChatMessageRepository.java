package ru.iprustam.trainee.simbirchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
//    void addMessage(ChatMessage message);
//    void deleteMessage(Integer messageId);
//    ChatMessage findMessageById(Integer messageId);
//    Collection<ChatMessage> findAllMessages();
}
