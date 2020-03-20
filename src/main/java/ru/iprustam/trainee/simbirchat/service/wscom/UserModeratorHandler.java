package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;

/**
 * Обработчик команды "Действия над модераторами"
 * //user moderator {login пользователя} - действия над модераторами.
 * -n - назначить пользователя модератором.
 * -d - “разжаловать” пользователя.
 */
public class UserModeratorHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        return false;
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) {
    }

    @Override
    protected String help() {
        return "//user moderator {login пользователя} - действия над модераторами.\n" +
                "-n - назначить пользователя модератором.\n" +
                "-d - “разжаловать” пользователя.";
    }
}
