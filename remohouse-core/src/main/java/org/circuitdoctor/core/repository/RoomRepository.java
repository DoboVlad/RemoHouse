package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.Room;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public interface RoomRepository extends Repository<Room,Long> {
    List<Room> findAllByLocation(Location location);
    Optional<Room> findById(Long id);
}
