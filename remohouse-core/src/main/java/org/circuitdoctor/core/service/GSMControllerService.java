package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.GSMController;

public interface GSMControllerService {
    GSMController setGSMControllerON(Long gsmID);
    GSMController setGSMControllerOFF(Long gsmID);
    String sendMessage(String message);
}
