package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;

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

        if(chatRoom.get().getOwner().getUserId() == currentUser.getUserId())
            return true;

        return false;
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) throws Exception {
        String roomName = chatCommand.getParam("remove");
        Long deletedCount = roomService.deleteRoom(roomName);
        if (deletedCount == 0)
            throw new Exception("None of the rooms deleted");

        messagingTemplate.convertAndSend(
                "/topic/room-concrete/" + chatCommand.getRoomId(),
                dtoTransport.objectToDto("room_remove", roomName));
    }

    @Override
    protected String help() {
        return "//room remove {Название комнаты}";
    }
}
