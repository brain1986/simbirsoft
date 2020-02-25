package ru.iprustam.trainee.simbirchat.repos;

import ru.iprustam.trainee.simbirchat.message.ChatMessage;

import java.util.Collection;

public interface ChatMessageRepository {
    void addMessage(ChatMessage message);
    void deleteMessage(Integer messageId);
    ChatMessage findMessageById(Integer messageId);
    Collection<ChatMessage> findAllMessages();
}
