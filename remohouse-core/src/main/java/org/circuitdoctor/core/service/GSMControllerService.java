package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.Room;

import java.util.List;

public interface GSMControllerService {
    GSMController setGSMControllerON(GSMController gsmCtrl);
    GSMController setGSMControllerOFF(GSMController gsmCtrl);
    GSMController addGSMController(GSMController gsmCtrl);
    GSMController updateGSMController(GSMController gsmCtrl);
    boolean deleteGSMController(Long gsmID);
    String sendMessage(String message);
    List<GSMController> findAllByRoom(Room room);
}
