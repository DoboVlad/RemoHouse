package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.GSMController;

public interface GSMControllerService {
    GSMController setGSMControllerON(GSMController gsmCtrl);
    GSMController setGSMControllerOFF(GSMController gsmCtrl);
    GSMController addGSMController(GSMController gsmCtrl);
    String sendMessage(String message);
}
