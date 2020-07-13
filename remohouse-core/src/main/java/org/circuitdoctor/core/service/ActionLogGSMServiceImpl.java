package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.ActionLogGSM;
import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.ActionLogGSMRepository;
import org.circuitdoctor.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        /*
        DESCR: adds an ActionLogGSM to database
        PARAM: actionLogGSM : ActionLogGSM
        PRE: actionLogGSM has to be a valid ActionLogGSM
        POST: returns saved actionLogGSM in database. The id will be changed based on the rule specified in BaseEntity
         */
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
    public List<ActionLogGSM> findAllActions(Long userID) {
        /*
        DESCR: returns a set of ActionLogGSM - the ActionLogGSMs corresponding to the userID {userID}
        PARAM: userID : Long
        PRE: userID > 0
        POST: -
         */
        log.trace("findAllActions -method entered userID={}",userID);
        List<ActionLogGSM> result = actionLogGSMRepository.findAll().stream()
                .filter(action -> action.getUser().getId().equals(userID))
                .collect(Collectors.toList());
        log.trace("findAllActions -method finished result={}",result);
        return result;
    }

    public List<ActionLogGSM> findAllActionsBeetwenDates(Long userID,String startDate,String endDate) {
        /*
        DESCR: returns a set of ActionLogGSM - the ActionLogGSMs corresponding to the userID {userID} and made beetwen the 2 dates
        PARAM: userID : Long,startDate : String,endDate : String
        PRE: userID > 0 ,valid dates
        POST: -
         */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start=LocalDateTime.parse(startDate,formatter);
        LocalDateTime end = LocalDateTime.parse(endDate,formatter);
        log.trace("findAllActions -method entered userID={}",userID);
        List<ActionLogGSM> result = actionLogGSMRepository.findAll().stream()
                .filter(action -> action.getUser().getId().equals(userID))
                .filter(action -> action.getDateTime().isAfter(start) && action.getDateTime().isBefore(end))
                .collect(Collectors.toList());
        log.trace("findAllActions -method finished result={}",result);
        return result;
    }

    @Override
    public void deleteActionsWithUser(User user) {
        /*
        DESCR: deletes actionLogGSMs with User {user} from database
        PARAM: user : User
        PRE: user has to have an already existing user id
        POST: actionLogs are deleted from database
         */
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
        /*
        DESCR: deletes actionLogGSMs with GSMController {gsmController} from database
        PARAM: gsmController : gsmController
        PRE: gsmController has to have an already existing gsmController id
        POST: actionLogs are deleted from database
         */
        log.trace("deleteActionsWithGSMController - method entered gsm={}",gsmController);
        List<ActionLogGSM> actionsToBeDeleted = actionLogGSMRepository.findAllByGsmController(gsmController);
        actionsToBeDeleted.forEach(action->{
            actionLogGSMRepository.delete(action);
            log.trace("deleteActionsWithGSMController - delete action a={}",action.getId());
        });
        log.trace("deleteActionsWithGSMController - method finished");
    }


}
