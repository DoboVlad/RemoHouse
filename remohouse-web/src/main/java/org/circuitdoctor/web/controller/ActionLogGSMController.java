package org.circuitdoctor.web.controller;

import org.circuitdoctor.core.model.ActionLogGSM;
import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.repository.RoomRepository;
import org.circuitdoctor.core.service.ActionLogGSMService;
import org.circuitdoctor.core.service.GSMControllerService;
import org.circuitdoctor.core.service.UserService;
import org.circuitdoctor.web.converter.ActionLogGSMConverter;
import org.circuitdoctor.web.converter.GSMControllerConverter;
import org.circuitdoctor.web.dto.ActionLogGSMDto;
import org.circuitdoctor.web.dto.LocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class ActionLogGSMController {
    private static final Logger log = LoggerFactory.getLogger(ActionLogGSMController.class);

    @Autowired
    private ActionLogGSMConverter actionLogGSMConverter;
    @Autowired
    private ActionLogGSMService actionLogGSMService;

    @RequestMapping(value = "actionLogGSM/getActions/{userID}",method = RequestMethod.GET)
    List<ActionLogGSMDto> getActions(@PathVariable Long userID){
        /*
        DESCR: gets all the actionLogGSMs of a user
        PARAM:userID - Long : If request is used, this is given in the path of the request
        PRE:userID>0
        POST: returns the set of actionLogGSMDto of the given user
         */
        log.trace("getActions - method entered userID={}",userID);
        List<ActionLogGSM> actions =actionLogGSMService.findAllActions(userID);
        log.trace("getActions - method finished l={}",actions);
        return actionLogGSMConverter.convertModelsToDtos(actions);
    }


}
