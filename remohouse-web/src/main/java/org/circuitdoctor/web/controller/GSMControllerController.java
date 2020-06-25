package org.circuitdoctor.web.controller;

import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.GSMStatus;
import org.circuitdoctor.core.service.GSMControllerService;
import org.circuitdoctor.web.converter.GSMControllerConverter;
import org.circuitdoctor.web.dto.GSMControllerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class GSMControllerController {
    private static final Logger log = LoggerFactory.getLogger(GSMControllerController.class);

    @Autowired
    private GSMControllerConverter gsmControllerConverter;
    @Autowired
    private GSMControllerService gsmControllerService;


    @RequestMapping(value = "gsm/addGSM/{userID}",method = RequestMethod.PUT)
    public String addGSMController(@RequestBody @Valid GSMControllerDto gsmControllerDto,@PathVariable Long userID, BindingResult errors){
        log.trace("addGSMController - method entered gsmControllerdto={}",gsmControllerDto);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("addRoom - validation error");
            return "validation errors";
        }
        GSMController gsmController=gsmControllerConverter.convertDtoToModel(gsmControllerDto);
        log.trace("gsmController id= "+gsmController.getId());
        if(userID.equals(gsmController.getRoom().getLocation().getUser().getId())) {
            GSMController g = gsmControllerService.addGSMController(gsmController);
            log.trace("addGSMController - method finished gsm={}", g);
            return String.valueOf(g.getId());
        }
        log.warn("addGSMController - {} has no access",userID);
        return "user has no access";
    }
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

        if(!userID.equals(gsmController.getRoom().getLocation().getUser().getId())){
            log.warn("openGSM -  user has no access to room");
            return "error: user has no access to this location";
        }

        //check if gsm is already opened
        if(gsmController.getStatus().equals(GSMStatus.ON)){
            log.warn("openGSM - gsmController is already opened");
            return "gsmController already opened";
        }

        String responseMessage=gsmControllerService.sendMessage("open");
        if(responseMessage.equals("ok")){
            GSMController g=gsmControllerService.setGSMControllerON(gsmController);
            log.trace("finished openGSM gsm={}",g);
            return "ok";
        }



        log.warn("something went wrong when the open message was sent");


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

        if(userID.equals(gsmController.getRoom().getLocation().getUser().getId())){
            log.warn("closeGSM -  user has no access to room");
            return "error: user has no access to this location";
        }

        //check if gsm is already closed
        if(gsmController.getStatus().equals(GSMStatus.OFF)){
            log.warn("closeGSM - gsmController is already closed");
            return "gsmController already closed";
        }


        String responseMessage=gsmControllerService.sendMessage("close");
        if(responseMessage.equals("ok")){
            GSMController g=gsmControllerService.setGSMControllerOFF(gsmController);
            log.trace("finished closeGSM gsm={}",g);
            return "ok";
        }





        log.warn("something went wrong when the close message was sent");
        return "something went wrong when the close message was sent";
    }
}
