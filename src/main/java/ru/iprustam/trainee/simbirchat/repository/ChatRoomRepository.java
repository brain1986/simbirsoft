package ru.iprustam.trainee.simbirchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
//    void addRoom(Room room);
//    void deleteRoom(Integer roomId);
//    Room findRoomById(Integer roomId);
//    Collection<Room> findAllRooms();
}
