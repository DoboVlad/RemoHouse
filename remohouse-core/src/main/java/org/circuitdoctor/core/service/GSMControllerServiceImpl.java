package org.circuitdoctor.core.service;


import org.apache.poi.hssf.util.HSSFColor;
import org.circuitdoctor.core.model.*;
import org.circuitdoctor.core.repository.GSMControllerRepository;
import org.circuitdoctor.core.repository.Repository;
import org.circuitdoctor.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
@Service
public class GSMControllerServiceImpl implements GSMControllerService {
    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);
    @Autowired
    private GSMControllerRepository gsmRepository;
    @Autowired
    private ActionLogGSMService actionLogGSMService;
    @Autowired
    private UserRepository userRepository;


    @Override
    public GSMController setGSMControllerON(GSMController gsmCtrl,Long userID) {
        /*
        DESCR: returns a GSMController = the GSMController updated with the status ON
        PARAM : gsmCtrl : GSMController, userID : long
        PRE : userID > 0,gsmCtrl has to have an already existing GSMController id
        POST: - returns the GSMController updated with status ON
         */
        log.trace("entered setGSMControllerON gsmCtrl={}",gsmCtrl);
        AtomicReference<GSMController> newGSMCtrl = new AtomicReference<>();
        Optional<GSMController> gsmFromDB = gsmRepository.findById(gsmCtrl.getId());

        //build the action Log
        ActionLogGSM actionLogGSM=ActionLogGSM.builder()
                .operationType("open")
                .dateTime(LocalDateTime.now())
                .gsmController(gsmCtrl)
                .user(userRepository.findById(userID).get())
                .build();

        gsmFromDB.ifPresent(gsmDB->{
            gsmDB.setStatus(GSMStatus.ON);
            gsmRepository.save(gsmDB);
            //add actionLog

            actionLogGSMService.addActionLogGSM(actionLogGSM);
            newGSMCtrl.set(gsmDB);
        });
        log.trace("finished setGSMControllerON gsmCtrl={}",newGSMCtrl);
        return newGSMCtrl.get();
    }

    @Override
    public GSMController setGSMControllerOFF(GSMController gsmCtrl,Long userID) {
        /*
        DESCR: returns a GSMController = the GSMController updated with the status OFF
        PARAM : gsmCtrl : GSMController, userID : long
        PRE : userID > 0,gsmCtrl has to have an already existing GSMController id
        POST: - returns the GSMController updated with status OFF
         */
        log.trace("entered setGSMControllerOFF gsmCtrl={}",gsmCtrl);
        AtomicReference<GSMController> newGSMCtrl = new AtomicReference<>();
        Optional<GSMController> gsmFromDB = gsmRepository.findById(gsmCtrl.getId());
        //build the action Log
        ActionLogGSM actionLogGSM=ActionLogGSM.builder()
                .operationType("close")
                .dateTime(LocalDateTime.now())
                .gsmController(gsmCtrl)
                .user(userRepository.findById(userID).get())
                .build();

        gsmFromDB.ifPresent(gsmDB->{
            gsmDB.setStatus(GSMStatus.OFF);
            gsmRepository.save(gsmDB);
            //add action log
            actionLogGSMService.addActionLogGSM(actionLogGSM);
            newGSMCtrl.set(gsmDB);
        });
        log.trace("finished setGSMControllerOFF");
        return newGSMCtrl.get();
    }

    @Override
    public GSMController addGSMController(@Valid GSMController gsmCtrl) {
        /*
        DESCR: adds a new GSMController in the database
        PARAM: gsmCtrl - GSMController
        PRE: gsmController has to be a valid GSMController
        POST: returns saved gsmController in database. The id will be changed based on the rule specified in BaseEntity
         */
        log.trace("addGSMController - method entered gsm={}",gsmCtrl);
        GSMController result =gsmRepository.save(gsmCtrl);
        log.trace("addGSMController - method finished newGSM={}",gsmCtrl);
        return result;
    }

    @Override
    public GSMController updateGSMController(GSMController gsmCtrl) {
        /*
        DESCR: updates a GSMController
        PARAM: gsmCtrl - GSMController
        PRE: gsmController has to have an already existing gsmController id
        POST: returns the gsmController updated
         */
        log.trace("updateGSMController - method entered gsm={}",gsmCtrl);
        gsmRepository.save(gsmCtrl);
        log.trace("updateGSMController - method finished newGSM={}",gsmCtrl);
        return gsmCtrl;
    }

    @Override
    public GSMController findByID(Long id) {
        /*
        DESCR: finds the GSMController with the id {id}
        PARAM: id - Long
        PRE: id>0
        POST: returns the gsmController found.
         */
        log.trace("findByID -gsm - method entered id={}",id);
        Optional<GSMController> result=gsmRepository.findById(id);
        log.trace("findByID -gsm - method finished gsm={}",result.get());
        return result.get();
    }

    @Override
    public boolean deleteGSMController(Long gsmID) {
        /*
        DESCR: deletes gsmController with id {gsmID} from database
        PARAM: gsmID - Long
        PRE: gsmID>0
        POST: returns true if the gsmController was successfully deleted
                      false if a gsmController with this id does not exist
              if gsmDeleted,all the actionLogs coresponding with this gsm are deleted.
         */
        log.trace("delete gsm - method entered id={}",gsmID);
        AtomicReference<Boolean> gsmFound = new AtomicReference<>(false);
        Optional<GSMController> gsmFromDB = gsmRepository.findById(gsmID);
        gsmFromDB.ifPresent(gsmDB->{
            actionLogGSMService.deleteActionsWithGSMController(gsmDB);
            gsmRepository.delete(gsmDB);

            gsmFound.set(true);
        });
        log.trace("delete gsm - method finished result={}",gsmFound.get());
        return gsmFound.get();
    }


    public String sendMessage(String message) {
        /*
        DESCR: send a message {message} to a phone number
        PARAM: message : String
        PRE: message most not be null
        POST: returns the result from the API
         */
        log.trace("sendMessage - method entered");
        ServiceUtils utils=new ServiceUtils();
        String result=utils.sendMessage(message,"0759021544");

        log.trace("sendMessage API- something is wrong");
        return result;
    }

    @Override
    public List<GSMController> findAllByRoom(Room room) {
        /*
        DESCR: returns a set of GSMController = the GSMs from the Room {room}
        PARAM : room : Room
        PRE : room has to have an already existing Room id
        POST: -
         */
        log.trace("findAllByRoom - method entered r-{}",room);
        List<GSMController> result = gsmRepository.findAllByRoom(room);
        log.trace("findAllByRoom - method finished r={}",result);
        return result;
    }

    @Override
    public void deleteGSMsWithRoom(Room room) {
        /*
        DESCR: deletes the gsmControllers from the Room {Room}
        PARAM: room - Room
        PRE: room has to have an already existing room id.
        POST: the gsmControllers from the Room room are deleted
         */
        log.trace("deleteGSMsWithRoom - method entered r={}",room);
        List<GSMController> controllersToDelete  = gsmRepository.findAllByRoom(room);
        controllersToDelete.stream().forEach(controller->{
            log.trace("deleteGSMsWithRoom - delete gsm={}",controller.getId());
            gsmRepository.delete(controller);
        });
        log.trace("deleteGSMsWithRoom - method finished");
    }
}
