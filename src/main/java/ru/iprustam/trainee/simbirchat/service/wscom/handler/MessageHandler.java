package ru.iprustam.trainee.simbirchat.service.wscom.handler;

public interface MessageHandler {
    MessageHandler setNext(MessageHandler handler);

    void handle(ChatCommand chatCommand);

    void helpCommandsToWs(ChatCommand chatCommand);
}
