package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.*;
import org.circuitdoctor.core.repository.ActionLogGSMRepository;
import org.circuitdoctor.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ActionLogGSMServiceImpl implements ActionLogGSMService {
    private static final Logger log = LoggerFactory.getLogger(ActionLogGSMServiceImpl.class);
    @Autowired
    private ActionLogGSMRepository actionLogGSMRepository;

    @Autowired
    private Repository<User,Long> userRepository;

    @Override
    public ActionLogGSM addActionLogGSM(@Valid ActionLogGSM actionLogGSM) {
        log.trace("addActionLogGSM - method entered action={}",actionLogGSM);
        AtomicReference<ActionLogGSM> actionLog = new AtomicReference<>();
        Optional<User> userFromDB = userRepository.findById(actionLogGSM.getUser().getId());
        userFromDB.ifPresent(user1 -> {
            ActionLogGSM result =actionLogGSMRepository.save(actionLogGSM);
            actionLog.set(result);
        });

        log.trace("addActionLogGSM - method finished newAction={}",actionLogGSM);
        return actionLog.get();
    }

    @Override
    public Set<ActionLogGSM> findAllActions(Long userID) {
        log.trace("findAllActions -method entered userID={}",userID);
        Set<ActionLogGSM> result = actionLogGSMRepository.findAll().stream()
                .filter(action -> action.getUser().getId().equals(userID))
                .collect(Collectors.toSet());
        log.trace("findAllActions -method finished result={}",result);
        return result;
    }

    @Override
    public void deleteActionsWithUser(User user) {
        log.trace("deleteActionsWithUser - method entered u={}",user);
        List<ActionLogGSM> actionsToBeDeleted = actionLogGSMRepository.findAllByUser(user);
        actionsToBeDeleted.forEach(action->{
            actionLogGSMRepository.delete(action);
            log.trace("deleteActionsWithUser - delete action a={}",action.getId());
        });
        log.trace("deleteActionsWithUser - method finished");
    }

    @Override
    public void deleteActionsWithGSMController(GSMController gsmController) {
        log.trace("deleteActionsWithGSMController - method entered gsm={}",gsmController);
        List<ActionLogGSM> actionsToBeDeleted = actionLogGSMRepository.findAllByGsmController(gsmController);
        actionsToBeDeleted.forEach(action->{
            actionLogGSMRepository.delete(action);
            log.trace("deleteActionsWithGSMController - delete action a={}",action.getId());
        });
        log.trace("deleteActionsWithGSMController - method finished");
    }
}
