package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.Room;

import java.util.List;
import java.util.Optional;

public interface GSMControllerRepository extends Repository<GSMController,Long> {
    List<GSMController> findAllByRoom(Room room);
    Optional<GSMController> findById(Long id);
}
