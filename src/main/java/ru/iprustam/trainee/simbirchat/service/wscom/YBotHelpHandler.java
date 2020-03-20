package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;

/**
 * Обработчик команды "Список доступных команд бота"
 * //yBot help
 */
public class YBotHelpHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        return false;
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) {
    }

    @Override
    protected String help() {
        return "//yBot help";
    }
}
