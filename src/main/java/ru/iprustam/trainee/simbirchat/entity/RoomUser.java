package ru.iprustam.trainee.simbirchat.entity;

import ru.iprustam.trainee.simbirchat.entity.composite.RoomUserCompositeId;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "room_user")
public class RoomUser {
    @EmbeddedId
    private RoomUserCompositeId id;

    @ManyToOne
    @MapsId("room")
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @ManyToOne
    @MapsId("user")
    @JoinColumn(name = "user_id")
    private ChatUser user;

    private ZonedDateTime blockUntil;

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
