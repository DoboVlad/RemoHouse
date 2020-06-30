package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.Room;

import java.util.List;

public interface RoomService {
    Room addRoom(Room room);
    Room updateRoom(Room room);
    List<Room> getRooms(Location location);
    boolean deleteRoom(Long room);
    void deleteRoomsWithLocation(Location location);
    boolean checkAccessRoom(Long id, Long roomID);
}
