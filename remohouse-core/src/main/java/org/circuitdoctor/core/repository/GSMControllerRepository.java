package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.Room;

import java.util.List;

public interface GSMControllerRepository extends Repository<GSMController,Long> {
    List<GSMController> findAllByRoom(Room room);
}
