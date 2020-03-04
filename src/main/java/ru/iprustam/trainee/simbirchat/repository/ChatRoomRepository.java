package ru.iprustam.trainee.simbirchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomTypes;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByRoomType(ChatRoomTypes roomType);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO room_user(room_id, user_id) VALUES (:roomId, :userId)")
    void addRoomUser(Long roomId, Long userId);

    @Transactional
    @Query(value = "SELECT room.* FROM room, room_user WHERE room.room_id=room_user.room_id AND room_user.user_id=:userId " +
            "ORDER BY room_id ASC",
            nativeQuery = true)
    List<ChatRoom> findByUserId(Long userId);
//    void addRoom(Room room);
//    void deleteRoom(Integer roomId);
//    Room findRoomById(Integer roomId);
//    Collection<Room> findAllRooms();
}