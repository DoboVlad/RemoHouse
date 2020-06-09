package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> getAllUsers() {
        log.trace("getAllUsers - method entered");
        List<User> result=userRepository.findAll();
        log.trace("getAllUsers - method finished result={}",result);
        return result;
    }
}
