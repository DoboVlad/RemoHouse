package org.circuitdoctor.core.service;


import org.circuitdoctor.core.model.*;
import org.circuitdoctor.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
@Service
public class GSMControllerServiceImpl implements GSMControllerService {
    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);
    @Autowired
    private Repository<GSMController,Long> gsmRepository;
    @Override
    public GSMController setGSMControllerON(GSMController gsmCtrl) {
        log.trace("entered setGSMControllerON gsmCtrl={}",gsmCtrl);
        AtomicReference<GSMController> newGSMCtrl = new AtomicReference<>();
        Optional<GSMController> gsmFromDB = gsmRepository.findById(gsmCtrl.getId());


        gsmFromDB.ifPresent(gsmDB->{
            gsmDB.setStatus(GSMStatus.ON);
            gsmRepository.save(gsmDB);
            newGSMCtrl.set(gsmDB);
        });
        log.trace("finished setGSMControllerON gsmCtrl={}",newGSMCtrl);
        return newGSMCtrl.get();
    }

    @Override
    public GSMController setGSMControllerOFF(GSMController gsmCtrl) {
        log.trace("entered setGSMControllerOFF gsmCtrl={}",gsmCtrl);
        AtomicReference<GSMController> newGSMCtrl = new AtomicReference<>();
        Optional<GSMController> gsmFromDB = gsmRepository.findById(gsmCtrl.getId());


        gsmFromDB.ifPresent(gsmDB->{
            gsmDB.setStatus(GSMStatus.OFF);
            gsmRepository.save(gsmDB);
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


    public String sendMessage(String message) {
        log.trace("sendMessage - method entered");
        String user="579042";
        String parola="ca00ddee9a532243928f16c22b49002b";
        String telefon="0759021544";
        String text=message;


        StringBuilder command = new StringBuilder("curl -X POST https://www.clickphone.ro/api/sms");

        StringBuilder param=new StringBuilder();
        param.append(" --data ").append("user=").append(user);
        param.append(" --data ").append("parola=").append(parola);
        param.append(" --data ").append("telefon=").append(telefon);
        param.append(" --data ").append("text=").append(text);
        command.append(param);
        System.out.println(command);


        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command.toString());
            process.waitFor();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            StringBuilder response=new StringBuilder();
            try {
                while ((line = input.readLine()) != null)
                    response.append(line).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response.toString().contains("<result>success</result>")){
                return "ok";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        }



        log.trace("sendMessage API- something is wrong");
        return "something went wrong";
    }
}
