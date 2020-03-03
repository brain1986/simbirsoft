package ru.iprustam.trainee.simbirchat.controller.transport;

public class Packet <T>{

    private T data;
    private String eventType;

    public String getEventType() {
        return eventType;
    }

    public T getData() {
        return data;
    }

    public Packet(String eventType, T data) {
        this.eventType = eventType;
        this.data = data;
    }
}
