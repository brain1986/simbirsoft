package ru.iprustam.trainee.simbirchat.controller.transport;

import ru.iprustam.trainee.simbirchat.entity.ChatRoom;

import java.util.List;

public class Transport {
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

}
