package ru.iprustam.trainee.simbirchat.handler.userhandler;

import ru.iprustam.trainee.simbirchat.handler.Handler;
import ru.iprustam.trainee.simbirchat.handler.handleresult.HandleResult;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;

public class DeleteModerator extends Handler {
    @Override
    public HandleResult doHandle(ChatMessage chatMessage) {
        return new HandleResult();
    }
}
