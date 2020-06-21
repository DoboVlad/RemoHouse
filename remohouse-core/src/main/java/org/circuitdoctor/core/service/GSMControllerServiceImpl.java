package org.circuitdoctor.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.circuitdoctor.core.model.*;
import org.circuitdoctor.core.repository.GSMControllerRepository;
import org.circuitdoctor.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
@Service
public class GSMControllerServiceImpl implements GSMControllerService {
    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);
    @Autowired
    private GSMControllerRepository gsmRepository;
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
        var values = new HashMap<String, String>() {{
            put("user", "579042");
            put ("parola", "ca00ddee9a532243928f16c22b49002b");
            put("telefon","0759021544");
            put("text",message);
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = null;
        HttpResponse<String> response = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(values);


            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.clickphone.ro/api/sms"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();



            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            log.trace("response"+response.body());
            log.trace("sendMessage - method finished,response={}",response.body());
            if(response.body().contains("<result>succes</result>")){
                return "ok";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        log.trace("sendMessage API- something is wrong");
        return "something went wrong";
    }
}
