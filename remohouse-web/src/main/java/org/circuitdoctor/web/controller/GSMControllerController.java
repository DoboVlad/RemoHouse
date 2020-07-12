package org.circuitdoctor.web.controller;

import org.circuitdoctor.core.model.ActionLogGSM;
import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.GSMStatus;
import org.circuitdoctor.core.model.Room;
import org.circuitdoctor.core.repository.RoomRepository;
import org.circuitdoctor.core.service.GSMControllerService;
import org.circuitdoctor.web.converter.GSMControllerConverter;
import org.circuitdoctor.web.dto.GSMControllerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class GSMControllerController {
    private static final Logger log = LoggerFactory.getLogger(GSMControllerController.class);

    @Autowired
    private GSMControllerConverter gsmControllerConverter;
    @Autowired
    private GSMControllerService gsmControllerService;
    @Autowired
    private RoomRepository roomRepository;

    @RequestMapping(value = "gsm/getGSMs/{userID}/{roomID}",method = RequestMethod.GET)
    List<GSMControllerDto> getGSMs(@PathVariable Long userID, @PathVariable Long roomID){
        /*
        DESCR: gets all the gsmControllers of a user from a room
        PARAM:userID - Long : If request is used, this is given in the path of the request
                roomID - Long : If request is used, this is given in the path of the request
        PRE:userID>0 roomID>0
        POST: returns the set of gsmControllerDto of the given user and room
         */
        log.trace("getGSMs - method entered u={} r={}",userID, roomID);
        Optional<Room> roomOptional = roomRepository.findById(roomID);
        AtomicBoolean ok = new AtomicBoolean(false);
        AtomicReference<List<GSMController>> result = new AtomicReference<>();
        roomOptional.ifPresent(room->{
            if(!room.getLocation().getUser().getId().equals(userID)) {
                log.trace("user has no access here");
            }
            else{
                ok.set(true);
                result.set(gsmControllerService.findAllByRoom(room));
            }
        });
        if(!ok.get())
            return null;
        log.trace("getGSMs - method finished r={}",result.get());
        return gsmControllerConverter.convertModelsToDtos(result.get());
    }

    @RequestMapping(value = "gsm/addGSM/{userID}",method = RequestMethod.PUT)
    public String addGSMController(@RequestBody @Valid GSMControllerDto gsmControllerDto,@PathVariable Long userID, BindingResult errors){
        /*
        DESCR:adds a new GSMController
        PARAM:gsmControllerDto  - GSMController  : must be valid. If request is used, this is given in the body of the request
              userID       - Long         : If request is used, this is given in the path of the request
              bindingResult- BindingResult:If there are any error validations, this is where they will be stored.
        PRE:None of them null. GSMControllerDto must be valid. UserID>0
        POST: 405 if error validations - when request used
              "validation errors" if any validation errors occured
              "null" if there is no user with the given id
              "different user ids" if the user does not have access to the gsmController
              the id of the saved gsmCnotroller if success
         */
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
    boolean openGSM(@RequestBody @Valid GSMControllerDto gsmControllerDto, @PathVariable Long userID, @PathVariable String message, BindingResult errors){
        /*
        DESCR:updates a gsmController making the status to be ON
        PARAM:userID      - Long         : If request is used, this is given in the path of the request
              gsmControllerDto - GSMControllerDto  : If request is used, this is sent in the body of the request
              errors      - BindingResult: here will be stored all the validation erros
        PRE:None null, userID>0, gsmControllerDto is valid
        POST: return true if success
                     false if user has no access to the gsmContrller or gsm is already opened or update failed
         */
        log.trace("entered openGSM message={}",message);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("openGSM - validation error");
            return false;
        }
        //validate access of user to room
        GSMController gsmController=gsmControllerConverter.convertDtoToModel(gsmControllerDto);

        if(!userID.equals(gsmController.getRoom().getLocation().getUser().getId())){
            log.warn("openGSM -  user has no access to room");
            return false;
        }

        //check if gsm is already opened
        if(gsmController.getStatus().equals(GSMStatus.ON)){
            log.warn("openGSM - gsmController is already opened");
            return false;
        }


        //String responseMessage=gsmControllerService.sendMessage(message);

        //log.warn("something went wrong when the open message was sent");

        GSMController g=gsmControllerService.setGSMControllerON(gsmController,userID);
        log.trace("finished openGSM gsm={}",g);
        return true;
        //return "something went wrong when the open message was sent";
    }

    @RequestMapping(value = "gsm/close/{userID}/{message}", method = RequestMethod.PUT)
    boolean closeGSM(@RequestBody @Valid GSMControllerDto gsmControllerDto, @PathVariable Long userID, @PathVariable String message, BindingResult errors){
        /*
        DESCR:updates a gsmController making the status to be OFF
        PARAM:userID      - Long         : If request is used, this is given in the path of the request
              gsmControllerDto - GSMControllerDto  : If request is used, this is sent in the body of the request
              errors      - BindingResult: here will be stored all the validation erros
        PRE:None null, userID>0, gsmControllerDto is valid
        POST: return true if success
                     false if user has no access to the gsmContrller or gsm is already closed or update failed
         */
        log.trace("entered closeGSM message={}",message);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("closeGSM - validation error");
            return false;
        }
        //validate access of user to room
        GSMController gsmController=gsmControllerConverter.convertDtoToModel(gsmControllerDto);

        if(!userID.equals(gsmController.getRoom().getLocation().getUser().getId())){
            log.warn("closeGSM -  user has no access to room");
            return false;
        }

        //check if gsm is already closed
        log.trace("gsm={}",gsmController);
        if(gsmController.getStatus().equals(GSMStatus.OFF)){
            log.warn("closeGSM - gsmController is already closed");
            return false;
        }


        //String responseMessage=gsmControllerService.sendMessage(message);
//        if(responseMessage.equals("ok")){
//
//        }
        GSMController g=gsmControllerService.setGSMControllerOFF(gsmController,userID);
        log.trace("finished closeGSM gsm={}",g);
        return true;
        //log.warn("something went wrong when the close message was sent");
        //return "something went wrong when the close message was sent";
    }

    @RequestMapping(value = "gsm/update/{userID}", method = RequestMethod.PUT)
    String updateGSM(@RequestBody @Valid GSMControllerDto gsmControllerDto, @PathVariable Long userID, BindingResult errors){
        /*
        DESCR:updates a gsmController
        PARAM:userID      - Long         : If request is used, this is given in the path of the request
              gsmControllerDto - GsmControllerDto  : If request is used, this is sent in the body of the request
              errors      - BindingResult: here will be stored all the validation erros
        PRE:None null, userID>0, gsmControllerDto is valid
        POST: return success essage if success
                     return error message if user has no access to the gsm or update failed
         */
        log.trace("entered updateGSM gsmDTO={}",gsmControllerDto);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("updateGSM - validation error");
            return "validation error";
        }
        //validate access of user to room
        if(gsmControllerService.findByID(gsmControllerDto.getId()).getStatus().equals(GSMStatus.ON)){
            gsmControllerDto.setStatus(GSMStatus.ON);
        }
        GSMController gsmController=gsmControllerConverter.convertDtoToModel(gsmControllerDto);

        if(!userID.equals(gsmController.getRoom().getLocation().getUser().getId())){
            log.warn("updateGSM -  user has no access to room");
            return "user has no acces to room";
        }

        GSMController g=gsmControllerService.updateGSMController(gsmController);
        log.trace("finished updateGSM gsm={}",g);
        return "gsm updated";

    }

    @RequestMapping(value = "gsm/delete/{userID}/{gsmID}", method = RequestMethod.DELETE)
    String deleteGSM(@PathVariable Long gsmID, @PathVariable Long userID){
        /*
        DESCR: deletes a gsmController
        PARAM: userID     - Long : If request is used, this is given in the path of the request
               gsmID - Long : If request is used, this is given in the path of the request
        PRE:userID > 0, gsmID>0
        POST: returns success message if success
                      return error message if the user has no access to the gsm or if the delete failed
         */
        log.trace("entered deleteGSM gsmID={}",gsmID);
        if(!userID.equals(gsmControllerService.findByID(gsmID).getRoom().getLocation().getUser().getId())){
            log.warn("deleteGSM - user has no access to room");
            return "user has no access to room";
        }
        boolean result=gsmControllerService.deleteGSMController(gsmID);
        log.trace("finished deleteGSM result={}",result);
        return "gsm deleted";
    }

    @RequestMapping(value = "gsm/qrcode/{gsmId}", method = RequestMethod.GET)
    byte[] getQRCodeGSM(@PathVariable Long gsmId){
        /*
        DESCR:generates qrCode for GSMControllerDto -gsmControllerDto
        PARAM:gsmId Long : If request is used, this is sent in the body of the request
              errors      - BindingResult: here will be stored all the validation erros
        PRE:gsmId >0
        POST: returns the byte array of the qrCode
              returns null if there is no gsmController with the id gsmId

         */
        log.trace("entered getQRCodeGSM gsmId={}",gsmId);
        byte[] qrCode= gsmControllerService.getQRCode(gsmId);
        log.trace("finished getQRCode");

        return qrCode;
    }
}
