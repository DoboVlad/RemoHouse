package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.Room;

import java.util.List;

public interface GSMControllerService {
    GSMController setGSMControllerON(GSMController gsmCtrl);
    GSMController setGSMControllerOFF(GSMController gsmCtrl);
    GSMController addGSMController(GSMController gsmCtrl);
    String sendMessage(String message);
    List<GSMController> findAllByRoom(Room room);
}
