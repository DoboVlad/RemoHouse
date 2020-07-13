package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.LogSignIn;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.LogSignInRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LogSignInServiceImpl implements LogSignInService {
    private static final Logger log = LoggerFactory.getLogger(LogSignInServiceImpl.class);
    @Autowired
    private LogSignInRepository logSignInRepository;
    @Override
    public Set<LogSignIn> getLogs(User user) {
        /*
        DESCR: returns a set of LogSignIn - the LogSignIns corresponding to the user {user}
        PARAM: user : User
        PRE: user has to have an already existing user id
        POST: -
         */
        log.trace("getLogs - method entered user={}",user);
        Set<LogSignIn> result = this.logSignInRepository.findAllByUser(user);
        log.trace("getLogs - method finished r={}",result);
        return result;
    }

    @Override
    public LogSignIn addLog(LogSignIn logSignIn) {
        /*
        DESCR: adds LogSignIn to database
        PARAM: LogSignIn : LogSignIn
        PRE: logSignIn has to be a valid logSignIn
        POST: returns saved logSignIn in database. The id will be changed based on the rule specified in BaseEntity
         */
        log.trace("addLog - method entered l={}",logSignIn);
        LogSignIn r = this.logSignInRepository.save(logSignIn);
        log.trace("addLog - method finished r={}",logSignIn);
        return r;
    }
}
