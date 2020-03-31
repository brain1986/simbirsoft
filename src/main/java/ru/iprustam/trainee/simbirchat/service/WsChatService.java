package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.dto.DtoMapper;
import ru.iprustam.trainee.simbirchat.dto.DtoPacket;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.ChatCommand;
import ru.iprustam.trainee.simbirchat.service.wscom.handler.MessageHandler;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;
import ru.iprustam.trainee.simbirchat.util.wsevent.WsEvent;

import java.util.List;

/**
 * Бизнес логика работы между вебсокетом и сервисами работы с юзерами, комнатами и сообщениями
 */
@Service
public class WsChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private DtoMapper dtoMapper;
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
    public void setDtoMapper(DtoMapper dtoMapper) {
        this.dtoMapper = dtoMapper;
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

        ChatRoom defaultRoom = roomService
                .getOrCreateRoom(ChatRoomType.DEFAULT_PUBLIC_ROOM, "Общая комната", chatUser);
        if (defaultRoom != null) {
            if (!userService.isUserInRoom(defaultRoom, chatUser.getUserId())) {
                roomService.addUserToRoom(defaultRoom, chatUser);
            }
        }

        List<ChatRoom> chatRooms = roomService.getRoomsWhereUserIn(chatUser);

        DtoPacket packet = new DtoPacket(WsEvent.ROOM_LIST_FULL, dtoMapper.roomToDto(chatRooms));
        messagingTemplate.convertAndSend(
                "/user/" + chatUser.getUsername() + "/queue/rooms-common-events", packet);
    }

    /**
     * Подписывает юзера к комнате.
     * Отправляет в вебсокет все сообщения данной комнаты
     *
     * @param roomId
     */
    public void roomSubscribe(Long roomId) {
        ChatUser chatUser = UserUtils.getCurrentPrincipal();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);

        List<ChatMessage> messages = messageService.findMessages(chatRoom);

        DtoPacket packet = new DtoPacket(WsEvent.ROOM_ALL_MESSAGES, dtoMapper.msgToDto(messages));
        messagingTemplate.convertAndSend(
                "/user/" + chatUser.getUsername() + "/queue/rooms-common-events", packet);
    }

    /**
     * Обрабатывает входящие сообщения
     * Отправляет в вебсокет результаты выполнения команды либо текстовое сообщение
     *
     * @param message
     * @param roomId
     */
    public void incomeMessageHandle(ChatMessage message, Long roomId) {
        ChatCommand chatCommand = ChatCommand.createChatCommand(message, roomId);
        if (chatCommand.getCommand().equals("help"))
            messageHandler.helpCommandsToWs(chatCommand);
        else
            messageHandler.handle(chatCommand);
    }
}