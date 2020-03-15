package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;

/**
 * Обработчик команды "Текстовое сообщение"
 */
public class SendMessageHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        if (chatCommand.getCommand().equals("msg") && chatCommand.getParam("m") != null) {
            return userService.checkAuthority(
                    UserUtils.getCurrentPrincipal(), ChatAuthority.MESSAGE_SEND);
        } else return false;
    }

    @Override
    protected void doHandle(ChatCommand chatCommand, Long roomId) {
        ChatMessage chatMessage = chatCommand.getChatMessage();
        messageService.save(chatMessage);
    }
}
