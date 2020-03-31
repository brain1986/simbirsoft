package ru.iprustam.trainee.simbirchat.dto;

import ru.iprustam.trainee.simbirchat.util.wsevent.WsEvent;

public class DtoPacket<T> {
    private T data;
    private WsEvent eventType;

    public DtoPacket(WsEvent eventType, T data) {
        this.eventType = eventType;
        this.data = data;
    }

    public WsEvent getEventType() {
        return eventType;
    }

    public T getData() {
        return data;
    }
}
