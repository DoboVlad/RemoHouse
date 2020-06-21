package org.circuitdoctor.web.controller;

import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.GSMStatus;
import org.circuitdoctor.core.model.Room;
import org.circuitdoctor.core.service.GSMControllerService;
import org.circuitdoctor.core.service.LocationService;
import org.circuitdoctor.core.service.RoomService;
import org.circuitdoctor.web.converter.GSMControllerConverter;
import org.circuitdoctor.web.converter.RoomConverter;
import org.circuitdoctor.web.dto.GSMControllerDto;
import org.circuitdoctor.web.dto.RoomDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public class GSMControllerController {
    private static final Logger log = LoggerFactory.getLogger(RoomController.class);
    @Autowired
    private RoomService roomService;
    @Autowired
    private GSMControllerConverter gsmControllerConverter;
    @Autowired
    private GSMControllerService gsmControllerService;
    @Autowired
    private LocationService locationService;


    @RequestMapping(value = "gsm/open/{userID}/{message}", method = RequestMethod.PUT)
    String openGSM(@RequestBody @Valid GSMControllerDto gsmControllerDto, @PathVariable Long userID, @PathVariable String message, BindingResult errors){
        log.trace("entered openGSM message={}",message);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("openGSM - validation error");
            return "validation errors";
        }
        //validate access of user to room
        GSMController gsmController=gsmControllerConverter.convertDtoToModel(gsmControllerDto);
        boolean userHasAccessToThisLocation = locationService.checkAccessLocation(userID,gsmController.getRoom().getLocation().getId());
        if(!userHasAccessToThisLocation){
            log.trace("openGSM -  user has no access to room");
            return "error: user has no access to this location";
        }

        //check if gsm is already opened
        if(gsmController.getStatus().equals(GSMStatus.ON)){
            log.trace("openGSM - gsmController is already opened");
            return "gsmController already opened";
        }


        String responseMessage=gsmControllerService.sendMessage(message);
        if(responseMessage.equals("ok")){
            gsmControllerService.setGSMControllerON(gsmController.getId());
            return "ok";
        }
        log.trace("finished openGSM ");

        return "something went wrong when the open message was sent";
    }
    @RequestMapping(value = "gsm/close/{userID}/{message}", method = RequestMethod.PUT)
    String closeGSM(@RequestBody @Valid GSMControllerDto gsmControllerDto, @PathVariable Long userID, @PathVariable String message, BindingResult errors){
        log.trace("entered closeGSM message={}",message);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("closeGSM - validation error");
            return "validation errors";
        }
        //validate access of user to room
        GSMController gsmController=gsmControllerConverter.convertDtoToModel(gsmControllerDto);
        boolean userHasAccessToThisLocation = locationService.checkAccessLocation(userID,gsmController.getRoom().getLocation().getId());
        if(!userHasAccessToThisLocation){
            log.trace("openGSM -  user has no access to room");
            return "error: user has no access to this location";
        }

        //check if gsm is already closed
        if(gsmController.getStatus().equals(GSMStatus.OFF)){
            log.trace("openGSM - gsmController is already closed");
            return "gsmController already closed";
        }


        String responseMessage=gsmControllerService.sendMessage(message);
        if(responseMessage.equals("ok")){
            gsmControllerService.setGSMControllerOFF(gsmController.getId());
            return "ok";
        }
        log.trace("finished closeGSM ");

        return "something went wrong when the close message was sent";
    }
}
