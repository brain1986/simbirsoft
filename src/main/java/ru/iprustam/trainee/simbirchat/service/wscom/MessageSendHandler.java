package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;

/**
 * Обработчик команды "Текстовое сообщение"
 * //msg -m {Сообщение}
 */
public class MessageSendHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        if (!chatCommand.getCommand().equals("msg"))
            return false;
        if (chatCommand.getParam("m") == null)
            return false;

        return userService.checkAuthority(
                UserUtils.getCurrentPrincipal(), ChatAuthority.MESSAGE_SEND);
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) {
        ChatMessage chatMessage = chatCommand.getChatMessage();
        messageService.save(chatMessage);
    }

    @Override
    protected String help() {
        return "//msg -m {Сообщение}";
    }
}
