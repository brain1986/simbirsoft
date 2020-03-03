package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.controller.transport.ChatTransport;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomTypes;

import java.security.Principal;

@Service
public class WsServiceMediator {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomService roomService;
    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public WsServiceMediator(SimpMessagingTemplate messagingTemplate, RoomService roomService,
                             UserService userService, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.roomService = roomService;
        this.userService = userService;
        this.messageService = messageService;
    }

    /**
     * Добавляет подписываемого юзера в дефолтную комнату, если его там нет
     * Отправляет ему список комнат и пользователей внутри них
     * @param p
     */
    public void wsSubscribing(Principal p) {
        if(!userService.isUserInRoom(p, ChatRoomTypes.DEFAULT_PUBLIC_ROOM))
            userService.addUserToRoom(p, ChatRoomTypes.DEFAULT_PUBLIC_ROOM);

        messagingTemplate.convertAndSend(
                "/user/" + p.getName() + "/queue/rooms",
                ChatTransport.getPacket("room_list_full", roomService.getAllUserRooms(p)));
    }
}
