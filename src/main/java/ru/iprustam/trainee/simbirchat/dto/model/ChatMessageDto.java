package ru.iprustam.trainee.simbirchat.dto.model;

import java.time.ZonedDateTime;

public class MessagesDto {
    private Long messageId;
    private String message;
    private ZonedDateTime messageTime;
    private ChatRoomDto chatRoom;
    private ChatUserDto chatUser;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(ZonedDateTime messageTime) {
        this.messageTime = messageTime;
    }


    public ChatRoomDto getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoomDto chatRoom) {
        this.chatRoom = chatRoom;
    }

    public ChatUserDto getChatUser() {
        return chatUser;
    }

    public void setChatUser(ChatUserDto chatUser) {
        this.chatUser = chatUser;
    }
}
