package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.dto.DtoPacket;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;
import ru.iprustam.trainee.simbirchat.util.wsevent.WsEvent;

import java.util.Optional;

/**
 * Обработчик команды "Удаление комнаты"
 * //room remove {Название комнаты}
 */
public class RoomRemoveHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        if (!chatCommand.getCommand().equals("room"))
            return false;
        if (chatCommand.getParam("remove") == null)
            return false;

        ChatUser currentUser = UserUtils.getCurrentPrincipal();
        if (!userService.checkAuthority(currentUser, ChatAuthority.ROOM_REMOVE))
            return false;

        Optional<ChatRoom> chatRoom = roomService.findRoom(chatCommand.getParam("remove"));
        if (chatRoom.isEmpty())
            return false;

        if (currentUser.getRole().getRoleName().equals("ROLE_ADMIN"))
            return true;

        return chatRoom.get().getOwner().getUserId() == currentUser.getUserId();
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) throws Exception {
        String roomName = chatCommand.getParam("remove");
        roomService.deleteRoom(roomName);

        DtoPacket packet = new DtoPacket(WsEvent.ROOM_REMOVE, roomName);
        messagingTemplate.convertAndSend(
                "/topic/room-concrete/" + chatCommand.getRoomId(),
                packet);
    }

    @Override
    protected String help() {
        return "//room remove {Название комнаты}";
    }
}
