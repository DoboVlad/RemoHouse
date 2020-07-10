package org.circuitdoctor.web.controller;

import org.circuitdoctor.core.model.LogSignIn;
import org.circuitdoctor.core.repository.UserRepository;
import org.circuitdoctor.core.service.LogSignInService;
import org.circuitdoctor.web.converter.LogSignInConverter;
import org.circuitdoctor.web.dto.LogSignInDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class LogSignInController {
    private static final Logger log = LoggerFactory.getLogger(LogSignInController.class);
    @Autowired
    private LogSignInService logSignInService;
    @Autowired
    private LogSignInConverter  logSignInConverter;
    @Autowired
    private UserRepository userRepository;
    @RequestMapping(value = "/logSignIn/add",method = RequestMethod.PUT)
    public boolean addLog(@RequestBody LogSignInDto logSignInDto){
        log.trace("addLog - method entered l={}",logSignInDto);
        LocalDateTime date = LocalDateTime.now();
        logSignInDto.setDateTime(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logSignInService.addLog(logSignInConverter.convertDtoToModel(logSignInDto));
        log.trace("addLog - method finished");
        return true;
    }

    @RequestMapping(value = "/logSignIn/getAll/{userID}",method = RequestMethod.GET)
    public Set<LogSignInDto> getLogs(@PathVariable Long userID){
        log.trace("getLogs - method entered id={}",userID);
        AtomicReference<Set<LogSignIn>> r = new AtomicReference<>();
        userRepository.findById(userID).ifPresent(user->{
            r.set(logSignInService.getLogs(user));
        });
        log.trace("getLogs - method finished r={}",r);
        if(r.get()==null)
            return null;
        return logSignInConverter.convertModelsToDtos(r.get());
    }
}
