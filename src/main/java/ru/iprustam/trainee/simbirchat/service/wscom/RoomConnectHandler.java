package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.dto.DtoPacket;
import ru.iprustam.trainee.simbirchat.dto.model.ChatRoomDto;
import ru.iprustam.trainee.simbirchat.dto.model.ChatUserDto;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.entity.RoomUser;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;

/**
 * Обработчик команды "Войти в комнату"
 * //room connect {Название комнаты} - войти в комнату;
 * -l {login пользователя} - добавить пользователя в комнату
 */
public class RoomConnectHandler extends BaseMessageHandler {
    @Override
    protected boolean canHandle(ChatCommand chatCommand) {
        if (!chatCommand.getCommand().equals("room"))
            return false;
        if (chatCommand.getParam("connect") == null)
            return false;

        ChatUser currentUser = UserUtils.getCurrentPrincipal();
        if (!userService.checkAuthority(currentUser, ChatAuthority.ROOM_CONNECT))
            return false;

        Optional<ChatRoom> chatRoom = roomService.findRoom(chatCommand.getParam("connect"));
        if (chatRoom.isEmpty())
            return false;

        if (currentUser.getRole().getRoleName().equals("ROLE_ADMIN"))
            return true;

        if (currentUser.getRole().getRoleName().equals("ROLE_MODERATOR"))
            return true;

        if (chatRoom.get().getRoomType() == ChatRoomType.DEFAULT_PUBLIC_ROOM)
            return true;

        if (chatRoom.get().getRoomType() == ChatRoomType.PUBLIC_ROOM)
            return true;

        return chatRoom.get().getOwner().getUserId() == currentUser.getUserId();
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) throws Exception {
        String roomName = chatCommand.getParam("connect");
        ChatUser chatUser;
        if (chatCommand.getParam("l") != null)
            chatUser = userService.findUser(chatCommand.getParam("l"));
        else
            chatUser = userService.findUser(UserUtils.getCurrentPrincipal().getUsername());

        Optional<ChatRoom> chatRoomOptional = roomService.findRoom(roomName);
        if (chatRoomOptional.isEmpty())
            throw new Exception("There is no such room");

        ChatRoom chatRoom = chatRoomOptional.get();
        if (!userService.isUserInRoom(chatUser, chatRoom.getRoomId()))
            chatRoom = roomService.addUserToRoom(chatRoom, chatUser);
        else {
            // Проверить не заблокирован ли юзер
            Optional<RoomUser> roomUser = chatUser.getRoomsUsers().stream()
                    .filter(ru -> ru.getUser().getUserId() == chatUser.getUserId())
                    .filter(ru -> ru.getBlockUntil().isAfter(ZonedDateTime.now()))
                    .findAny();
            if (roomUser.isPresent())
                throw new Exception("This user blocked until " + roomUser.get().getBlockUntil());
            throw new Exception("This user is already in the room ");
        }

        // Добавляем к списку юзеров комнаты
        DtoPacket packet = dtoTransport.entitiesToDtoMap("room_connect",
                Arrays.asList("roomId", "chatUser"),
                Arrays.asList(chatRoom.getRoomId(), chatUser),
                Arrays.asList(null, ChatUserDto.class)
        );
        messagingTemplate.convertAndSend("/topic/room-concrete/" + chatCommand.getRoomId(), packet);

        // Загрузить комнату
        messagingTemplate.convertAndSend(
                "/user/" + chatUser.getUsername() + "/queue/rooms-common-events",
                dtoTransport.entityToDto("room_create", chatRoom, ChatRoomDto.class));
    }

    @Override
    protected String help() {
        return "//room connect {Название комнаты} - войти в комнату;\n" +
                "-l {login пользователя} - добавить пользователя в комнату";
    }
}
