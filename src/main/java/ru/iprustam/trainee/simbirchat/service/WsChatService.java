package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.dto.DtoTransport;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;
import ru.iprustam.trainee.simbirchat.util.room.RoomFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;

/**
 * Бизнес логика работы между вебсокетом и сервисами работы с юзерами, комнатами и сообщениями
 */
@Service
public class WsChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomService roomService;
    private final UserService userService;
    private final MessageService messageService;
    private final DtoTransport dtoTransport;

    @Autowired
    public WsChatService(SimpMessagingTemplate messagingTemplate, RoomService roomService,
                         UserService userService, MessageService messageService, DtoTransport dtoTransport) {
        this.messagingTemplate = messagingTemplate;
        this.roomService = roomService;
        this.userService = userService;
        this.messageService = messageService;
        this.dtoTransport = dtoTransport;
    }

    /**
     * Добавляет подписываемого юзера в дефолтную комнату, если его там нет
     * Отправляет в ответ список комнат и пользователей внутри них
     */
    public void subscribe() {
        ChatUser chatUser = (ChatUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

        //Отправить объекты комнат
        messagingTemplate.convertAndSend(
                "/user/" + chatUser.getUsername() + "/queue/rooms-common-events",
                dtoTransport.chatRoomsToDto("room_list_full", chatRooms));
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

    /**
     * Проверяет присутствие заданного пользователя в заданной комнате
     *
     * @param chatUser
     * @param roomId
     * @return
     */
    public boolean isUserInRoom(ChatUser chatUser, Long roomId) {
        return userService
                .findUsers(roomId)
                .stream().anyMatch(u -> u.getUserId() == chatUser.getUserId());
    }

    /**
     * Принимает сообщение от пользователя, записывает в базу и транслирует обратно в вебсокет
     *
     * @param message
     * @param roomId
     */
    @PreAuthorize("hasAuthority('MESSAGE_SEND')")
    public void messageSend(ChatMessage message, Long roomId) {
        ChatUser chatUser = (ChatUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        message.setChatUser(chatUser);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        message.setChatRoom(chatRoom);
        message.setMessageTime(ZonedDateTime.now(ZoneId.systemDefault()));

        messageService.save(message);

        messagingTemplate.convertAndSend(
                "/topic/room-concrete/" + roomId,
                dtoTransport.chatMessageToDto("new_message", message));
    }

    /**
     * Отправляет сообщения из БД пользователям при SUBSCRIBE к вебсокету
     *
     * @param roomId
     */
    @PreAuthorize("hasAuthority('MESSAGE_RECEIVE')")
    public void messagesLoad(Long roomId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);

        List<ChatMessage> messages = messageService.findMessages(chatRoom);

        messagingTemplate.convertAndSend(
                "/topic/room-concrete/" + roomId,
                dtoTransport.chatMessagesToDto("room_all_messages", messages));
    }
}