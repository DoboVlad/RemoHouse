package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.Room;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public interface GSMControllerRepository extends Repository<GSMController,Long> {
    List<GSMController> findAllByRoom(Room room);
    Optional<GSMController> findById(Long id);
}
