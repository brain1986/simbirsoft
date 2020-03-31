package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.dto.DtoPacket;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;
import ru.iprustam.trainee.simbirchat.util.wsevent.WsEvent;

import java.util.Map;
import java.util.Optional;

/**
 * Обработчик команды "Переименование пользователя"
 * //user rename -o {старый login пользователя} -n {новый логин пользователя}
 */
public class UserRenameHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        if (!chatCommand.getCommand().equals("user"))
            return false;
        if (!chatCommand.hasParam("rename"))
            return false;
        if (chatCommand.getParam("o") == null)
            return false;
        if (chatCommand.getParam("n") == null)
            return false;

        ChatUser currentUser = UserUtils.getCurrentPrincipal();
        if (!userService.checkAuthority(currentUser, ChatAuthority.USER_RENAME))
            return false;

        if (userService.findUser(chatCommand.getParam("o")) == null)
            return false;

        if (userService.findUser(chatCommand.getParam("n")) != null)
            return false;

        if (currentUser.getRole().getRoleName().equals("ROLE_ADMIN"))
            return true;

        return currentUser.getUsername().equals(chatCommand.getParam("o"));
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) {

        String oP = chatCommand.getParam("o");
        String nP = chatCommand.getParam("n");

        // Update in DB
        ChatUser chatUserDB = userService.findUser(oP);
        chatUserDB.setUsername(nP);
        userService.save(chatUserDB);

        // Update in session registry
        Optional<ChatUser> chatUserSessionOptional = sessionRegistry.getAllPrincipals().stream()
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
                .filter(u -> ((ChatUser) u).getUsername().equals(oP))
                .map(u -> (ChatUser) u)
                .findAny();

        if (chatUserSessionOptional.isPresent()) {
            ChatUser chatUserSession = chatUserSessionOptional.get();
            chatUserSession.setUsername(nP);
        }

        DtoPacket packet = new DtoPacket(WsEvent.USER_RENAME,
                Map.of("userId", chatUserDB.getUserId(), "new_username", nP));
        messagingTemplate.convertAndSend(
                "/topic/events-for-all", packet);

    }

    @Override
    protected String help() {
        return "//user rename -o {старый login пользователя} -n {новый логин пользователя}";
    }
}
