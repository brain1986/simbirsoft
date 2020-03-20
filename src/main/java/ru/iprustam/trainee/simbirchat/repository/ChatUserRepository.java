package ru.iprustam.trainee.simbirchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;

import java.util.List;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    ChatUser findByUsernameIgnoreCase(String username);

    @Query(value = "SELECT usr.*, room_user.block_until FROM room_user, usr " +
            "WHERE usr.user_id=room_user.user_id AND room_id=:roomId",
            nativeQuery = true)
    List<ChatUser> findByRoomId(Long roomId);
}
