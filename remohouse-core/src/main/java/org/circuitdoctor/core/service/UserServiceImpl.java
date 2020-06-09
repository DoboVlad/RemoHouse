package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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

    @Override
    public boolean login(User user) {
        log.trace("login - method entered user={}",user);
        String phoneNo = user.getPhoneNumber();
        String password = user.getPassword();
        String email = user.getEmail();
        AtomicBoolean result = new AtomicBoolean(false);
        Optional<User> userFromDB = userRepository.findById(user.getId());
        userFromDB.ifPresent(userDB->{
            if((userDB.getPhoneNumber().equals(phoneNo) || userDB.getEmail().equals(email)) && userDB.getPassword().equals(password))
                result.set(true);
        });
        log.trace("login - method finished r={}",result.get());
        return result.get();
    }

    @Override
    public User signUp(User user) {
        log.trace("signUp - method entered user={}",user);


        User newUser=userRepository.save(user);
        
        log.trace("signUp - method finished newUser={}",newUser);
        return newUser;
    }
}
