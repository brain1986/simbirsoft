package ru.iprustam.trainee.simbirchat.util.room;

import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;

public class RoomFactory {
    public static ChatRoom createChatRoom(ChatRoomType chatRoomType, String roomName, ChatUser chatUser) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomType(chatRoomType);
        chatRoom.setRoomName(roomName);
        chatRoom.setOwner(chatUser);
        return chatRoom;
    }
}
