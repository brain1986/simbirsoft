package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.entity.RoomUser;
import ru.iprustam.trainee.simbirchat.repository.ChatRoomRepository;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;
import ru.iprustam.trainee.simbirchat.util.room.RoomFactory;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Предоставляет сервис работы с комнатами
 */
@Service
public class RoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public RoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    /**
     * Возвращает комнаты по заданному типу комнаты
     *
     * @param chatRoomType
     * @return
     */
    public List<ChatRoom> findRooms(ChatRoomType chatRoomType) {
        return chatRoomRepository.findByRoomType(chatRoomType);
    }

    public Optional<ChatRoom> findRoom(Long roomId) {
        return chatRoomRepository.findById(roomId);
    }

    public Optional<ChatRoom> findRoom(String name) {
        return chatRoomRepository.findByRoomNameIgnoreCase(name);
    }

    public Long deleteRoom(String name) {
        return chatRoomRepository.deleteByRoomNameIgnoreCase(name);
    }

    public ChatRoom addUserToRoom(ChatRoom chatRoom, ChatUser chatUser) {
        RoomUser roomUser = new RoomUser();
        roomUser.setRoom(chatRoom);
        roomUser.setUser(chatUser);
        Set<RoomUser> roomsUsers = Set.of(roomUser);
        chatRoom.setRoomsUsers(roomsUsers);
        chatRoomRepository.addRoomUser(chatRoom.getRoomId(), chatUser.getUserId());
        return chatRoom;
    }

    /**
     * Получить все комнаты, где состоит заданный юзер
     *
     * @param chatUser
     * @return
     */
    public List<ChatRoom> getRoomsWhereUserIn(ChatUser chatUser) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserId(chatUser.getUserId());
        chatRooms = chatRooms.stream()
                .filter(r ->
                        // Комнаты, где залогиненный юзер заблочен не отображать
                        r.getRoomsUsers().stream()
                                .noneMatch(ru -> ru.getUser().getUserId() == chatUser.getUserId()
                                        && ru.getBlockUntil().isAfter(ZonedDateTime.now())
                                )
                )
                .peek(r -> {
                    // заблоченных юзеров не показывать
                    Predicate<RoomUser> blocked = ru -> ru.getBlockUntil().isAfter(ZonedDateTime.now());
                    r.getRoomsUsers().removeIf(blocked);
                })
                .collect(Collectors.toList());

        return chatRooms;
    }

    public ChatRoom getOrCreateRoom(ChatRoomType chatRoomType, String roomName, ChatUser chatUser) {
        List<ChatRoom> rooms = findRooms(chatRoomType);

        if (!rooms.isEmpty()) {
            return rooms.get(0);
        } else {
            ChatRoom defaultRoom = RoomFactory
                    .createChatRoom(ChatRoomType.DEFAULT_PUBLIC_ROOM, roomName, chatUser);
            save(defaultRoom);
            return defaultRoom;
        }
    }

    public ChatRoom deleteUserFromRoom(ChatRoom chatRoom, ChatUser chatUser) {
        Predicate<RoomUser> isQualified = ru -> ru.getUser().getUserId() == chatUser.getUserId();
        chatRoom.getRoomsUsers().removeIf(isQualified);
        chatRoomRepository.deleteUserFromRoom(chatRoom.getRoomId(), chatUser.getUserId());
        return chatRoom;
    }

    public ChatRoom blockUserInRoom(ChatRoom chatRoom, ChatUser chatUser, int minutes) {
        Predicate<RoomUser> isQualified = ru -> ru.getUser().getUserId() == chatUser.getUserId();
        chatRoom.getRoomsUsers().stream()
                .filter(isQualified)
                .forEach(ru -> ru.setBlockUntil(ZonedDateTime.now().plusMinutes(minutes)));
        chatRoom = chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    public void save(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }
}
