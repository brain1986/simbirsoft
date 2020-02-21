package ru.iprustam.trainee.simbirchat.websocket.models;

public class WsIngoingMessage {

    private String message;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public WsIngoingMessage() {
    }

    public WsIngoingMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
