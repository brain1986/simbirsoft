package ru.iprustam.trainee.simbirchat.handlers.userhandlers;

import ru.iprustam.trainee.simbirchat.handlers.Handler;
import ru.iprustam.trainee.simbirchat.handlers.handleresults.HandleResult;
import ru.iprustam.trainee.simbirchat.message.ChatMessage;

public class UnlockUser extends Handler {
    @Override
    public HandleResult doHandle(ChatMessage chatMessage) {
        return new HandleResult();
    }

    protected boolean canHandle(ChatMessage chatMessage) {
        return false;
    }
}