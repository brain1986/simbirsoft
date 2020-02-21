package ru.iprustam.trainee.simbirchat.websocket.models;

public class WsOutgoingMessage {
    private Integer roomId;
    private String user;
    private String message;
    private String time;

    public WsOutgoingMessage() {
    }

    public WsOutgoingMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

//    public static WsOutgoingMessage exportFromChatMessage(ChatMessage chatMessage) {
//        WsOutgoingMessage outMessage = new WsOutgoingMessage(chatMessage.getMessage());
//        outMessage.setRoomId(chatMessage.getRoomId());
//        outMessage.setUser(chatMessage.getUserId());
//        outMessage.setTime(chatMessage.getTime());
//        return new WsOutgoingMessage();
//    }
}