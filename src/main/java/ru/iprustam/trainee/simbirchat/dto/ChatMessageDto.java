package ru.iprustam.trainee.simbirchat.dto;

import java.time.ZonedDateTime;

public class ChatMessageDto {
    private Long messageId;
    private String message;
    private ZonedDateTime messageTime;
    private Long roomId;
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

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public ChatUserDto getChatUser() {
        return chatUser;
    }

    public void setChatUser(ChatUserDto chatUser) {
        this.chatUser = chatUser;
    }
}
