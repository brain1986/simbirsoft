package ru.iprustam.trainee.simbirchat.user;

import ru.iprustam.trainee.simbirchat.handlers.Handler;

public abstract class ChatUser {
    private int userId;
    private Handler userActions;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Handler getUserActions() {
        return userActions;
    }

    public void setUserActions(Handler userActions) {
        this.userActions = userActions;
    }
}
