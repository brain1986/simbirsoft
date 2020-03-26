package ru.iprustam.trainee.simbirchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.util.room.ChatRoomType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByRoomTypeAndIsDeletedFalse(ChatRoomType roomType);

    Optional<ChatRoom> findByRoomNameIgnoreCaseAndIsDeletedFalse(String roomName);

    @Query(value = "SELECT room.* FROM room_user LEFT OUTER JOIN room " +
            "ON room_user.room_id=room.room_id " +
            "WHERE room_user.user_id=:userId AND room.is_deleted = false " +
            "ORDER BY room_id ASC",
            nativeQuery = true)
    List<ChatRoom> findByUserId(Long userId);
}
