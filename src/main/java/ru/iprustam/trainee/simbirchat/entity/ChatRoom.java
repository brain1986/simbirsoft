package ru.iprustam.trainee.simbirchat.entity;

import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private ChatRoomType roomType;
    private String roomName;

    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    private ChatUser owner;

    @ManyToMany
    @JoinTable(
            name = "room_user",
            joinColumns = {@JoinColumn(name = "room_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<ChatUser> users;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private Set<ChatMessage> messages;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public ChatRoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(ChatRoomType roomType) {
        this.roomType = roomType;
    }

    public Set<ChatUser> getUsers() {
        return users;
    }

    public void setUsers(Set<ChatUser> users) {
        this.users = users;
    }

    public Set<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<ChatMessage> messages) {
        this.messages = messages;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ChatUser getOwner() {
        return owner;
    }

    public void setOwner(ChatUser owner) {
        this.owner = owner;
    }
}
