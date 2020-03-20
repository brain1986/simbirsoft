package ru.iprustam.trainee.simbirchat.dto.model;

import java.time.ZonedDateTime;

public class RoomUserDto extends Dto {
    private ChatUserDto user;
    private ZonedDateTime blockUntil;

    public ChatUserDto getUser() {
        return user;
    }

    public void setUser(ChatUserDto user) {
        this.user = user;
    }

    public ZonedDateTime getBlockUntil() {
        return blockUntil;
    }

    public void setBlockUntil(ZonedDateTime blockUntil) {
        this.blockUntil = blockUntil;
    }
}
