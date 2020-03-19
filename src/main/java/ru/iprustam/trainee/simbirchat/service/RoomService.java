package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.repository.ChatRoomRepository;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;
import ru.iprustam.trainee.simbirchat.util.room.RoomFactory;

import java.util.List;
import java.util.Optional;

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

    public void addUserToRoom(ChatRoom chatRoom, ChatUser chatUser) {
        chatRoomRepository.addRoomUser(chatRoom.getRoomId(), chatUser.getUserId());
    }

    /**
     * Получить все комнаты, где состоит заданный юзер
     *
     * @param chatUser
     * @return
     */
    public List<ChatRoom> getRoomsWhereUserIn(ChatUser chatUser) {
        return chatRoomRepository.findByUserId(chatUser.getUserId());
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

    public void save(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }
}
