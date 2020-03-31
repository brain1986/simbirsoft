package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.dto.DtoPacket;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;
import ru.iprustam.trainee.simbirchat.util.room.RoomFactory;
import ru.iprustam.trainee.simbirchat.util.wsevent.WsEvent;

import java.util.Optional;
import java.util.Set;

/**
 * Обработчик команды "Создание комнаты"
 * //room create {Название комнаты}
 * -c закрытая комната.
 */
public class RoomCreateHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        if (!chatCommand.getCommand().equals("room"))
            return false;
        if (chatCommand.getParam("create") == null)
            return false;

        if (chatCommand.hasParam("c"))
            return userService.checkAuthority(
                    UserUtils.getCurrentPrincipal(), ChatAuthority.PRIVATE_ROOM_CREATE);
        else
            return userService.checkAuthority(
                    UserUtils.getCurrentPrincipal(), ChatAuthority.PUBLIC_ROOM_CREATE);
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) throws Exception {
        // Проверить есть ли такая комната
        String roomName = chatCommand.getParam("create");
        Optional<ChatRoom> chatRoomOptional = roomService.findRoom(roomName);
        if (chatRoomOptional.isPresent())
            throw new Exception("Room with this name already exists");

        ChatUser chatUser = UserUtils.getCurrentPrincipal();
        ChatRoom chatRoom;
        ChatRoomType chatRoomType =
                (chatCommand.hasParam("c")) ? ChatRoomType.PRIVATE_ROOM : ChatRoomType.PUBLIC_ROOM;
        chatRoom = RoomFactory.createChatRoom(chatRoomType, roomName, chatUser);
        chatRoom.setUsers(Set.of(chatUser));
        roomService.save(chatRoom);

        DtoPacket packet = new DtoPacket(WsEvent.ROOM_CREATE, dtoMapper.roomToDto(chatRoom));
        messagingTemplate.convertAndSend(
                "/user/" + chatUser.getUsername() + "/queue/rooms-common-events", packet
        );
    }

    @Override
    protected String help() {
        return "//room create {Название комнаты}\n" +
                "-c закрытая комната.";
    }
}
