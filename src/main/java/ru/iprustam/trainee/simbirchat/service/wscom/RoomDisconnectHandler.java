package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;

/**
 * Обработчик команды "Выйти из текущей комнаты"
 * //room disconnect {Название комнаты} - выйти из заданной комнаты;
 * -l {login пользователя} - выгоняет пользователя из комнаты (для владельца,
 * модератора и админа).
 * -m {Количество минут} - время на которое пользователь не сможет войти (для
 * владельца, модератора и админа).
 */
public class RoomDisconnectHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        return false;
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) {
    }

    @Override
    protected String help() {
        return "//room disconnect {Название комнаты} - выйти из заданной комнаты;\n" +
                "-l {login пользователя} - выгоняет пользователя из комнаты (для владельца,\n" +
                "модератора и админа).\n" +
                "-m {Количество минут} - время на которое пользователь не сможет войти (для\n" +
                "владельца, модератора и админа).";
    }
}
