package ru.iprustam.trainee.simbirchat.repos;

import ru.iprustam.trainee.simbirchat.room.Room;

import java.util.Collection;

public interface RoomRepository {
    void addRoom(Room room);
    void deleteRoom(Integer roomId);
    Room findRoomById(Integer roomId);
    Collection<Room> findAllRooms();
}
