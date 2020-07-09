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
        log.trace("addGSMController - method entered gsm={}",gsmCtrl);
        GSMController result =gsmRepository.save(gsmCtrl);
        log.trace("addGSMController - method finished newGSM={}",gsmCtrl);
        return result;
    }

    @Override
    public GSMController updateGSMController(GSMController gsmCtrl) {
        log.trace("updateGSMController - method entered gsm={}",gsmCtrl);
        gsmRepository.save(gsmCtrl);
        log.trace("updateGSMController - method finished newGSM={}",gsmCtrl);
        return gsmCtrl;
    }

    @Override
    public GSMController findByID(Long id) {
        log.trace("findByID -gsm - method entered id={}",id);
        Optional<GSMController> result=gsmRepository.findById(id);
        log.trace("findByID -gsm - method finished gsm={}",result.get());
        return result.get();
    }

    @Override
    public boolean deleteGSMController(Long gsmID) {
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

        log.trace("sendMessage - method entered");
        ServiceUtils utils=new ServiceUtils();
        String result=utils.sendMessage(message,"0759021544");

        log.trace("sendMessage API- something is wrong");
        return result;
    }

    @Override
    public List<GSMController> findAllByRoom(Room room) {
        log.trace("findAllByRoom - method entered r-{}",room);
        List<GSMController> result = gsmRepository.findAllByRoom(room);
        log.trace("findAllByRoom - method finished r={}",result);
        return result;
    }

    @Override
    public void deleteGSMsWithRoom(Room room) {
        log.trace("deleteGSMsWithRoom - method entered r={}",room);
        List<GSMController> controllersToDelete  = gsmRepository.findAllByRoom(room);
        controllersToDelete.stream().forEach(controller->{
            log.trace("deleteGSMsWithRoom - delete gsm={}",controller.getId());
            gsmRepository.delete(controller);
        });
        log.trace("deleteGSMsWithRoom - method finished");
    }
}
