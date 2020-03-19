package ru.iprustam.trainee.simbirchat.dto.model;

import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;

import java.util.Set;

public class ChatRoomDto extends Dto {
    private Long roomId;
    private ChatRoomType roomType;
    private String roomName;
    private Set<ChatUserDto> users;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<ChatUserDto> getUsers() {
        return users;
    }

    public void setUsers(Set<ChatUserDto> users) {
        this.users = users;
    }

    public ChatRoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(ChatRoomType roomType) {
        this.roomType = roomType;
    }
}
