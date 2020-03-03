package ru.iprustam.trainee.simbirchat.controller.transport;

import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;

import java.util.List;
import java.util.stream.Collectors;

public class ChatTransport {
    public static Packet<List<ChatRoom>> getPacket(String eventName, List<ChatRoom> data) {

        data.forEach(
                room -> {
                    room.setMessages(null);
                    room.getUsers().forEach(
                            user->{
                                user.setPassword("");
                                user.setMessages(null);
                                user.setRooms(null);
                                user.getRole().setUsers(null);
                            });
                });

        return new Packet(eventName, data);
    }

    public static Packet<ChatMessage> getPacket(String eventName, ChatMessage data) {
        return new Packet(eventName, messageClean(data));
    }

    public static Packet<List<ChatMessage>> getPacketM(String eventName, List<ChatMessage> data) {
        List<ChatMessage> messages = data.stream().map(m->messageClean(m)).collect(Collectors.toList());
        return new Packet(eventName, messages);
    }

    private static ChatMessage messageClean(ChatMessage message) {
        var room = message.getChatRoom();
        room.setUsers(null);
        room.setMessages(null);

        var user = message.getChatUser();
        user.setPassword("");
        user.setRooms(null);
        user.setMessages(null);
        user.getRole().setUsers(null);

        return message;
    }

    public static Packet<ChatMessage> getPacket(String eventName, Object data) {
        return new Packet(eventName, data);
    }
}
