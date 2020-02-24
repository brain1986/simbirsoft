package ru.iprustam.trainee.simbirchat.handlers.userhandlers;

import ru.iprustam.trainee.simbirchat.handlers.Handler;
import ru.iprustam.trainee.simbirchat.handlers.handleresults.HandleResult;
import ru.iprustam.trainee.simbirchat.message.ChatMessage;

public class LockUser extends Handler {
    @Override
    public HandleResult doHandle(ChatMessage chatMessage) {
        return new HandleResult();
    }
}