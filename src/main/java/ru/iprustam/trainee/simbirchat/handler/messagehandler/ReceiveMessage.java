package ru.iprustam.trainee.simbirchat.handler.messagehandler;

import ru.iprustam.trainee.simbirchat.handler.Handler;
import ru.iprustam.trainee.simbirchat.handler.handleresult.HandleResult;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;

public class ReceiveMessage extends Handler {
    @Override
    public HandleResult doHandle(ChatMessage chatMessage) {
        return new HandleResult();
    }
}