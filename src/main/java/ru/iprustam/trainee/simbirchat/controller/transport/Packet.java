package ru.iprustam.trainee.simbirchat.controller.transport;

public class Packet<T> {

    private T data;
    private String eventType;

    public Packet(String eventType, T data) {
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
