package ru.iprustam.trainee.simbirchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatRoomUserBlock;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
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
        return chatRoomRepository.findByRoomTypeAndIsDeletedFalse(chatRoomType);
    }

    public Optional<ChatRoom> findRoom(Long roomId) {
        return chatRoomRepository.findById(roomId);
    }

    public Optional<ChatRoom> findRoom(String name) {
        return chatRoomRepository.findByRoomNameIgnoreCaseAndIsDeletedFalse(name);
    }

    public void deleteRoom(String name) {
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomNameIgnoreCaseAndIsDeletedFalse(name);
        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            chatRoom.setDeleted(true);
            save(chatRoom);
        }
    }

    public ChatRoom addUserToRoom(ChatRoom chatRoom, ChatUser chatUser) {
        chatRoom.getUsers().add(chatUser);
        chatRoomRepository.save(chatRoom);
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
                        r.getUsersBlock().stream()
                                .noneMatch(ru -> ru.getUser().getUserId() == chatUser.getUserId()
                                        && ru.getBlockUntil().isAfter(ZonedDateTime.now())
                                )
                )
                .peek(r -> {
                    // заблоченных юзеров не показывать
                    List<ChatUser> blockedUsers = r.getUsersBlock().stream().map(ub -> ub.getUser())
                            .collect(Collectors.toList());

                    Predicate<ChatUser> blocked = u -> blockedUsers.stream().anyMatch(ub -> ub.getUserId() == u.getUserId()); //u.getBlockUntil().isAfter(ZonedDateTime.now());
                    r.getUsers().removeIf(blocked);
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
        chatRoom.getUsers().removeIf(u -> u.getUserId() == chatUser.getUserId());
        chatRoom.getUsersBlock().removeIf(u -> u.getUser().getUserId() == chatUser.getUserId());
        chatRoomRepository.save(chatRoom);

        return chatRoom;
    }

    public ChatRoom blockUserInRoom(ChatRoom chatRoom, ChatUser chatUser, int minutes) {
        ChatRoomUserBlock chatRoomUserBlock = new ChatRoomUserBlock();
        chatRoomUserBlock.setRoom(chatRoom);
        chatRoomUserBlock.setUser(chatUser);
        chatRoomUserBlock.setBlockUntil(ZonedDateTime.now().plusMinutes(minutes));
        Set<ChatRoomUserBlock> blocks = chatRoom.getUsersBlock();
        blocks.removeIf(u -> u.getUser().getUserId() == chatUser.getUserId());
        blocks.add(chatRoomUserBlock);

        chatRoom = chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    public void save(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }
}
