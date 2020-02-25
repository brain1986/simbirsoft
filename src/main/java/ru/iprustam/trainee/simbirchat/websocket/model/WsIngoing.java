package ru.iprustam.trainee.simbirchat.websocket.models;

public class WsIngoing {

    private String message;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public WsIngoing() {
    }

    public WsIngoing(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
