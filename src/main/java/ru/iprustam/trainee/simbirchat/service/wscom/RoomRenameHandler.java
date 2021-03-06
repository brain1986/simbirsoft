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
 * Обработчик команды "Переименование комнаты"
 * //room rename {Новое название}
 */
public class RoomRenameHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        if (!chatCommand.getCommand().equals("room"))
            return false;
        if (chatCommand.getParam("rename") == null)
            return false;

        ChatUser currentUser = UserUtils.getCurrentPrincipal();
        if (!userService.checkAuthority(currentUser, ChatAuthority.ROOM_RENAME))
            return false;

        Optional<ChatRoom> chatRoom = roomService.findRoom(chatCommand.getRoomId());
        if (chatRoom.isEmpty())
            return false;

        if (currentUser.getRole().getRoleName().equals("ROLE_ADMIN"))
            return true;

        return chatRoom.get().getOwner().getUserId() == currentUser.getUserId();
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) throws Exception {
        Long roomId = chatCommand.getRoomId();
        String roomNameNew = chatCommand.getParam("rename");
        Optional<ChatRoom> chatRoomOldOptional = roomService.findRoom(roomId);
        Optional<ChatRoom> chatRoomNewOptional = roomService.findRoom(roomNameNew);
        if (chatRoomOldOptional.isEmpty())
            throw new Exception("There is no such room");
        if (chatRoomNewOptional.isPresent())
            throw new Exception("Room with this name already exists");

        ChatRoom chatRoom = chatRoomOldOptional.get();
        chatRoom.setRoomName(roomNameNew);
        roomService.save(chatRoom);

        DtoPacket packet = new DtoPacket(WsEvent.ROOM_RENAME, dtoMapper.roomToDto(chatRoom));
        messagingTemplate.convertAndSend(
                "/topic/room-concrete/" + chatCommand.getRoomId(), packet);
    }

    @Override
    protected String help() {
        return "//room rename {Новое название}";
    }
}
