package ru.iprustam.trainee.simbirchat.repos;

import org.springframework.stereotype.Component;
import ru.iprustam.trainee.simbirchat.message.ChatMessage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ChatMessageRepo implements ChatMessageRepository {
    private Map<Integer, ChatMessage> messages = new HashMap<>();
    private static AtomicInteger newMessageId = new AtomicInteger(1);

    @Override
    public void addMessage(ChatMessage message) {
        messages.put(newMessageId.getAndAdd(1), message);
    }

    @Override
    public void deleteMessage(Integer messageId) {
        messages.remove(messageId);
    }

    @Override
    public ChatMessage findMessageById(Integer messageId) {
        return messages.get(messageId);
    }

    @Override
    public Collection<ChatMessage> findAllMessages() {
        return messages.values();
    }
}
