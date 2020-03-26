package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.dto.DtoPacket;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatRoomUserBlock;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;
import ru.iprustam.trainee.simbirchat.util.wsevent.WsEvent;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

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

        // Очистить заблокированных юзеров, у которых срок блока истек
        Predicate<ChatRoomUserBlock> qualify = ub -> ub.getBlockUntil().isBefore(ZonedDateTime.now());
        chatRoom.getUsersBlock().removeIf(qualify);
        roomService.save(chatRoom);

        // Проверить не заблокирован ли юзер
        Predicate<ChatRoomUserBlock> p = u -> u.getUser().getUserId() == chatUser.getUserId()
                && u.getBlockUntil().isAfter(ZonedDateTime.now());

        Optional<ChatRoomUserBlock> block = chatRoom.getUsersBlock().stream().filter(p).findAny();
        if (block.isPresent())
            throw new Exception("This user blocked until " + block.get().getBlockUntil());

        if (!userService.isUserInRoom(chatRoom, chatUser.getUserId())) {
            chatRoom.getUsers().add(chatUser);
            roomService.save(chatRoom);
        } else throw new Exception("This user is already in the room ");


        // Добавляем к списку юзеров комнаты
        DtoPacket packet = new DtoPacket(WsEvent.ROOM_CONNECT,
                Map.of("roomId", chatRoom.getRoomId(), "chatUser", dtoMapper.userToDto(chatUser)));
        messagingTemplate.convertAndSend("/topic/room-concrete/" + chatCommand.getRoomId(), packet);

        // Загрузить комнату
        packet = new DtoPacket(WsEvent.ROOM_CREATE, dtoMapper.roomToDto(chatRoom));
        messagingTemplate.convertAndSend(
                "/user/" + chatUser.getUsername() + "/queue/rooms-common-events", packet);
    }

    @Override
    protected String help() {
        return "//room connect {Название комнаты} - войти в комнату;\n" +
                "-l {login пользователя} - добавить пользователя в комнату";
    }
}
