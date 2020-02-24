package ru.iprustam.trainee.simbirchat.repos;

import org.springframework.stereotype.Component;
import ru.iprustam.trainee.simbirchat.room.Room;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RoomRepo implements RoomRepository {

    private Map<Integer, Room> rooms = new HashMap<>();
    private static AtomicInteger newRoomId = new AtomicInteger(1);

    @Override
    public void addRoom(Room room) {
        rooms.put(newRoomId.getAndAdd(1), room);
    }

    @Override
    public void deleteRoom(Integer roomId) {
        rooms.remove(roomId);
    }

    @Override
    public Room findRoomById(Integer roomId) {
        return rooms.get(roomId);
    }

    @Override
    public Collection<Room> findAllRooms() {
        return rooms.values();
    }
}
