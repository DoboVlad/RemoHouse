package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends Repository<Room,Long> {
    List<Room> findAllByLocation(Location location);
    Optional<Room> findById(Long id);
}
