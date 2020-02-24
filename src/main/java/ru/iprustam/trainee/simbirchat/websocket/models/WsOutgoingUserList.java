package ru.iprustam.trainee.simbirchat.websocket.models;

import java.util.List;

public class WsOutgoingUserList extends WsOutgoing {
    private List<String> usersList;

    public WsOutgoingUserList(List<String> usersList) {
        this.setType("users-event");
        this.usersList = usersList;
    }

    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<String> usersList) {
        this.usersList = usersList;
    }
}
