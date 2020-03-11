package ru.iprustam.trainee.simbirchat.dto;

public class DtoPacket<T> {
    private T data;
    private String eventType;

    public DtoPacket(String eventType, T data) {
        this.eventType = eventType;
        this.data = data;
    }

    public String getEventType() {
        return eventType;
    }

    public T getData() {
        return data;
    }
}
