package ru.iprustam.trainee.simbirchat.service.wscom;

import ru.iprustam.trainee.simbirchat.dto.DtoPacket;
import ru.iprustam.trainee.simbirchat.dto.model.ChatUserDto;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.BaseMessageHandler;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;

import java.util.Arrays;
import java.util.Optional;

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
        if (!chatCommand.getCommand().equals("room"))
            return false;
        if (!chatCommand.hasParam("disconnect"))
            return false;

        ChatUser currentUser = UserUtils.getCurrentPrincipal();
        if (!userService.checkAuthority(currentUser, ChatAuthority.USER_DELETE))
            return false;

        Optional<ChatRoom> chatRoom;
        if (chatCommand.getParam("disconnect") != null) {
            chatRoom = roomService.findRoom(chatCommand.getParam("disconnect"));
            if (chatRoom.isEmpty())
                return false;
        } else
            chatRoom = roomService.findRoom(chatCommand.getRoomId());

        // Админов нельзя блочить
        if (chatCommand.getParam("l") != null) {
            ChatUser userToDisconnect = userService.findUser(chatCommand.getParam("l"));
            if (userToDisconnect.getRole().getRoleName().equals("ROLE_ADMIN"))
                return false;
            if (userToDisconnect.getRole().getRoleName().equals("ROLE_MODERATOR"))
                return false;
        }

        if (chatCommand.getParam("l") == null && chatCommand.getParam("m") == null)
            return true;

        if (chatCommand.getParam("l") != null || chatCommand.getParam("m") != null) {
            if (currentUser.getRole().getRoleName().equals("ROLE_ADMIN"))
                return true;

            if (currentUser.getRole().getRoleName().equals("ROLE_MODERATOR"))
                return true;

            if (chatRoom.get().getOwner().getUserId() == currentUser.getUserId())
                return true;
        }

        return false;
    }

    @Override
    protected void doHandle(ChatCommand chatCommand) throws Exception {
        String disconnectP = chatCommand.getParam("disconnect");
        String lP = chatCommand.getParam("l");
        String mP = chatCommand.getParam("m");

        ChatUser chatUser;
        if (lP != null)
            chatUser = userService.findUser(lP);
        else
            chatUser = UserUtils.getCurrentPrincipal();

        Optional<ChatRoom> chatRoomOptional;
        if (disconnectP != null)
            chatRoomOptional = roomService.findRoom(disconnectP);
        else
            chatRoomOptional = roomService.findRoom(chatCommand.getRoomId());

        if (chatRoomOptional.isEmpty())
            throw new Exception("Room hasn't been found");

        ChatRoom chatRoom = chatRoomOptional.get();

        // Удалить из таблицы room_user, либо заблокировать по полю block_until
        if ((lP == null && mP == null) || (lP != null && mP == null)) {
            chatRoom = roomService.deleteUserFromRoom(chatRoom, chatUser);
        } else if (lP != null && mP != null) {
            int minutes = Integer.parseInt(mP);
            chatRoom = roomService.blockUserInRoom(chatRoom, chatUser, minutes);
        }

        // Отправить сообщение об отсоединении
        DtoPacket packet = dtoTransport.entitiesToDtoMap("room_disconnect",
                Arrays.asList("roomId", "chatUser"),
                Arrays.asList(chatRoom.getRoomId(), chatUser),
                Arrays.asList(null, ChatUserDto.class)
        );
        messagingTemplate.convertAndSend(
                "/topic/room-concrete/" + chatRoom.getRoomId(), packet
        );
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
