package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;

/**
 * Обработчик команды "Поиск ролика"
 * //yBot find -k -l {название канала}||{название видео} - в ответ бот присылает
 * ссылку на ролик;
 * -v - выводит количество текущих просмотров.
 * -l - выводит количество лайков под видео
 */
public class YBotFindHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        return false;
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) {
    }

    @Override
    protected String help() {
        return "//yBot find -k -l {название канала}||{название видео} - в ответ бот присылает\n" +
                "ссылку на ролик;\n" +
                "-v - выводит количество текущих просмотров.\n" +
                "-l - выводит количество лайков под видео";
    }
}
