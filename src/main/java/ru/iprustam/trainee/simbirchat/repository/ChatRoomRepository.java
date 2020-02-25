package ru.iprustam.trainee.simbirchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iprustam.trainee.simbirchat.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
//    void addRoom(Room room);
//    void deleteRoom(Integer roomId);
//    Room findRoomById(Integer roomId);
//    Collection<Room> findAllRooms();
}
