package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.ActionLogGSM;
import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.User;

import java.util.List;
import java.util.Set;

public interface ActionLogGSMService {
    ActionLogGSM addActionLogGSM(ActionLogGSM actionLogGSM);
    Set<ActionLogGSM> findAllActions(Long userID);
    void deleteActionsWithUser(User user);
    void deleteActionsWithGSMController(GSMController gsmController);
}
