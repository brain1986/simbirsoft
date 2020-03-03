package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.controller.transport.ChatTransport;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomTypes;

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

    @Autowired
    public WsChatService(SimpMessagingTemplate messagingTemplate, RoomService roomService,
                         UserService userService, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.roomService = roomService;
        this.userService = userService;
        this.messageService = messageService;
    }

    /**
     * Добавляет подписываемого юзера в дефолтную комнату, если его там нет
     * Отправляет в ответ список комнат и пользователей внутри них
     */
    public void subscribe() {
        ChatUser chatUser = (ChatUser) UserService.getLoggedUserDetails();

        ChatRoom defaultRoom = getDefaultRoom();
        if(defaultRoom != null) {
            if (!isUserInRoom(chatUser, defaultRoom)) {
                roomService.addUserToRoom(defaultRoom, chatUser);
            }
        }

        List<ChatRoom> chatRooms = roomService.getAllUserRooms(chatUser);
        for(var room : chatRooms) {
            room.setUsers(new HashSet<>(userService.findUsers(room.getRoomId())));
        }

        //Отправить объекты комнат
        messagingTemplate.convertAndSend(
                "/user/" + chatUser.getUsername() + "/queue/rooms-common-events",
                ChatTransport.getPacket("room_list_full", chatRooms));
    }

    private ChatRoom getDefaultRoom()  {
        List<ChatRoom> rooms = roomService.findRooms(ChatRoomTypes.DEFAULT_PUBLIC_ROOM);
        if(rooms.size() != 0) {
            return rooms.get(0);
        }

        throw new IllegalArgumentException("No default room found");
    }

    private boolean isUserInRoom(ChatUser chatUser, ChatRoom chatRoom) {
        return userService
                .findUsers(chatRoom.getRoomId())
                .stream().anyMatch(u->u.getUserId() == chatUser.getUserId());

    }

    /**
     * Проверяет, имеет ли заданный пользователь доступ к заданной комнате
     * @param roomId
     * @param chatUser
     */
    public void tryAccessRoom(Long roomId, ChatUser chatUser) {
        List<ChatRoom> chatRooms = roomService.getAllUserRooms(chatUser);
        for(var room : chatRooms) {
            room.setUsers(new HashSet<>(userService.findUsers(room.getRoomId())));
        }

        boolean hasUser = chatRooms.stream()
                .filter(r->r.getRoomId() == roomId)
                .flatMap(s->s.getUsers().stream())
                .anyMatch(u->u.getUserId() == chatUser.getUserId());

        if(!hasUser)
            throw new IllegalArgumentException("User is not in the roomId="+roomId);
    }

    /**
     * Принимает сообщение от пользователя, записывает в базу и транслирует обратно в вебсокет
     * @param message
     * @param roomId
     */
    @PreAuthorize("hasAuthority('MESSAGE_SEND')")
    public void messageSend(ChatMessage message, Long roomId) {
        ChatUser chatUser = (ChatUser) UserService.getLoggedUserDetails();

        message.setChatUser(chatUser);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        message.setChatRoom(chatRoom);
        message.setMessageTime(ZonedDateTime.now(ZoneId.systemDefault()));

        messageService.save(message);

        messagingTemplate.convertAndSend(
                "/topic/room-concrete/"+roomId,
                ChatTransport.getPacket("new_message", message));
    }

    /**
     * Отправляет сообщения из БД пользователям при SUBSCRIBE к вебсокету
     * @param roomId
     */
    @PreAuthorize("hasAuthority('MESSAGE_RECEIVE')")
    public void messagesLoad(Long roomId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);

        List<ChatMessage> messages = messageService.findMessages(chatRoom);

        messagingTemplate.convertAndSend(
                "/topic/room-concrete/"+roomId,
                ChatTransport.getPacketM("room_all_messages", messages));
    }
}