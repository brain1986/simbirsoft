package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.dto.DtoTransport;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.MessageHandler;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;
import ru.iprustam.trainee.simbirchat.util.room.RoomFactory;

import java.util.HashSet;
import java.util.List;

/**
 * Бизнес логика работы между вебсокетом и сервисами работы с юзерами, комнатами и сообщениями
 */
@Service
public class WsChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private DtoTransport dtoTransport;
    private RoomService roomService;
    private MessageService messageService;
    private MessageHandler messageHandler;

    @Autowired
    public WsChatService(SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Autowired
    public void setDtoTransport(DtoTransport dtoTransport) {
        this.dtoTransport = dtoTransport;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setMessageHandler(@Qualifier("configuredMessageHandler") MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Подписывает юзера в дефолтную комнату, если его там нет.
     * Отправляет в вебсокет список комнат и пользователей внутри них
     */
    public void subscribe() {
        ChatUser chatUser = UserUtils.getCurrentPrincipal();

        ChatRoom defaultRoom = getDefaultPublicRoom(chatUser);
        if (defaultRoom != null) {
            if (!isUserInRoom(chatUser, defaultRoom.getRoomId())) {
                roomService.addUserToRoom(defaultRoom, chatUser);
            }
        }

        List<ChatRoom> chatRooms = roomService.getRoomsWhereUserIn(chatUser);
        for (var room : chatRooms) {
            room.setUsers(new HashSet<>(userService.findUsers(room.getRoomId())));
        }

        messagingTemplate.convertAndSend(
                "/user/" + chatUser.getUsername() + "/queue/rooms-common-events",
                dtoTransport.chatRoomsToDto("room_list_full", chatRooms));
    }

    /**
     * Подписывает юзера к комнате.
     * Отправляет в вебсокет все сообщения данной комнаты
     *
     * @param roomId
     */
    public void roomSubscribe(Long roomId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);

        List<ChatMessage> messages = messageService.findMessages(chatRoom);

        messagingTemplate.convertAndSend(
                "/topic/room-concrete/" + roomId,
                dtoTransport.chatMessagesToDto("room_all_messages", messages));
    }

    /**
     * Обрабатывает входящие сообщения
     * Отправляет в вебсокет результаты выполнения команды либо текстовое сообщение
     *
     * @param message
     * @param roomId
     */
    public void incomeMessageHandle(ChatMessage message, Long roomId) throws Exception {
        ChatCommand chatCommand = ChatCommand.createChatCommand(message);
        messageHandler.handle(chatCommand, roomId);
    }

    private ChatRoom getDefaultPublicRoom(ChatUser chatUser) {
        List<ChatRoom> rooms = roomService.findRooms(ChatRoomType.DEFAULT_PUBLIC_ROOM);

        if (!rooms.isEmpty()) {
            return rooms.get(0);
        } else {
            ChatRoom defaultRoom = RoomFactory.createDefaultPublicRoom(chatUser);
            roomService.save(defaultRoom);
            return defaultRoom;
        }
    }

    private boolean isUserInRoom(ChatUser chatUser, Long roomId) {
        return userService
                .findUsers(roomId)
                .stream().anyMatch(u -> u.getUserId() == chatUser.getUserId());
    }
}