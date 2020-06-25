package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.Room;

import java.util.List;

public interface RoomRepository extends Repository<Room,Long> {
    List<Room> findAllByLocation(Location location);
}
