package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;

/**
 * Обработчик команды "Выгнать пользователя из комнаты"
 * //user ban;
 * -l {login пользователя} - выгоняет пользователя из всех комнат
 * -m {Количество минут} - время на которое пользователь не сможет войти
 */
public class UserBanHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        return false;
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) {
    }

    @Override
    protected String help() {
        return "//user ban;\n" +
                "-l {login пользователя} - выгоняет пользователя из всех комнат\n" +
                "-m {Количество минут} - время на которое пользователь не сможет войти";
    }
}
