package ru.iprustam.trainee.simbirchat.message;

import java.time.ZonedDateTime;

/** Этот класс часть паттерна Bridge
 * Представляет собой сообщение для чата. Содержит внутри себя
 * возможность вставить дополнительный ресурс, типа видео
 */
public abstract class ChatMessage {
    private int messageId;
    private int roomId;
    private int userId;
    private String message;
    private ZonedDateTime time;

    public MessageResource getResource() {
        return resource;
    }

    public void setResource(MessageResource resource) {
        this.resource = resource;
    }

    private MessageResource resource;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }
}
