package org.circuitdoctor.core.service;







import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.UserRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActionLogGSMService actionLogGSMService;
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
        Optional<User> userFromDB = userRepository.findAllByEmail(user.getEmail());
        log.trace("email");
        userFromDB.ifPresent(userDB->{
            log.trace(userDB.toString());
            if((userDB.getPhoneNumber().equals(phoneNo) || userDB.getEmail().equals(email)) && userDB.getPassword().equals(password))
                result.set(true);
        });
        if(!result.get()){
            log.trace("phone number");
            userFromDB = userRepository.findAllByPhoneNumber(user.getPhoneNumber());
            userFromDB.ifPresent(userDB->{
                log.trace(userDB.toString());
                if((userDB.getPhoneNumber().equals(phoneNo) || userDB.getEmail().equals(email)) && userDB.getPassword().equals(password))
                    result.set(true);
            });
        }
        log.trace("login - method finished r={}",result.get());
        return result.get();
    }

    @Override
    public User signUp(@Valid User user) {
        log.trace("signUp - method entered user={}",user);
        User newUser=userRepository.save(user);

        log.trace("signUp - method finished newUser={}",newUser);
        return newUser;
    }

    @Override
    public User changePassword(User user) {
        log.trace("changePassword - method entered user={}",user);

        AtomicReference<User> newUser = new AtomicReference<>();
        Optional<User> userFromDB = userRepository.findById(user.getId());
        if(userFromDB.get().getPassword().length()<7){
            log.trace("changePassord - invalid Password size(<7)");
            return userFromDB.get();

        }

        userFromDB.ifPresent(userDB->{
            userDB.setPassword(user.getPassword());
            userRepository.save(userDB);
            newUser.set(userDB);
        });

        log.trace("\"changePassword - method finished user={}",newUser);
        return newUser.get();

    }

    @Override
    public Optional<User> getUserByCredential(String credential) {
        log.trace("getUserByCredential - method entered c={}",credential);
        Optional<User> result;
        if(credential.contains("@"))
            result =  userRepository.findAllByEmailStartsWith(credential);
        else
            result = userRepository.findAllByPhoneNumber(credential);
        log.trace("getUserByCredential - method finished r={}",result);
        return result;
    }



}
