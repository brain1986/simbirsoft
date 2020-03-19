package ru.iprustam.trainee.simbirchat.entity.composite;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RoomUserCompositeId implements Serializable {
    @Column(name = "room_id")
    private Long room;

    @Column(name = "user_id")
    private Long user;

    public Long getRoom() {
        return room;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
