package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.repository.ChatMessageRepository;

import java.util.List;

/**
 * Предоставляет сервис работы с сообщениями
 */
@Service
public class MessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public MessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public void save(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    public List<ChatMessage> findMessages(ChatRoom chatRoom) {
        return chatMessageRepository.findByChatRoom(chatRoom);
    }
}
