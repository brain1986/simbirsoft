package ru.iprustam.trainee.simbirchat.entity;

import javax.persistence.*;
import java.time.ZonedDateTime;

/** Этот класс часть паттерна Bridge
 * Представляет собой сообщение для чата. Содержит внутри себя
 * возможность вставить дополнительный ресурс, типа видео
 */
@Entity
@Table(name = "message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private String message;
    private ZonedDateTime messageTime;

    @ManyToOne
    @JoinColumn(name="room_id", nullable=false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private ChatUser chatUser;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
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


}
