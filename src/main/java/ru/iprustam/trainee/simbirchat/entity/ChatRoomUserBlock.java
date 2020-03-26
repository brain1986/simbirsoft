package ru.iprustam.trainee.simbirchat.entity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "room_user_block")
public class ChatRoomUserBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ChatUser user;

    private ZonedDateTime blockUntil;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatRoom getRoom() {
        return room;
    }

    public void setRoom(ChatRoom room) {
        this.room = room;
    }

    public ChatUser getUser() {
        return user;
    }

    public void setUser(ChatUser user) {
        this.user = user;
    }

    public ZonedDateTime getBlockUntil() {
        return blockUntil;
    }

    public void setBlockUntil(ZonedDateTime blockUntil) {
        this.blockUntil = blockUntil;
    }
}
