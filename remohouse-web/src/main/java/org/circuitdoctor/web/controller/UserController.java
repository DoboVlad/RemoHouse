package org.circuitdoctor.web.controller;

import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.Repository;
import org.circuitdoctor.core.service.UserService;
import org.circuitdoctor.web.converter.UserConverter;
import org.circuitdoctor.web.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class UserController {
    /*
    If the validation fails, it will trigger a MethodArgumentNotValidException.
    By default, Spring will translate this exception to an HTTP status 400 (Bad Request)
     */
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserConverter userConverter;

    @RequestMapping(value = "user/login", method = RequestMethod.PUT)
    boolean login(@Valid  @RequestBody UserDto userDto){
        //send the user's ID from db!!
        log.trace("login - method entered user={}",userDto);
        User user = userConverter.convertDtoToModel(userDto);
        AtomicBoolean result = new AtomicBoolean(false);
        result.set(userService.login(user));
        log.trace("login - method finished result={}",result.get());
        return result.get();
    }
    @RequestMapping(value = "user/signUp", method = RequestMethod.POST)
    UserDto signUp(@Valid @RequestBody UserDto userDto){
        log.trace("signUp - method entered user={}",userDto);
        User user = userConverter.convertDtoToModel(userDto);
        UserDto result= userConverter.convertModelToDto(userService.signUp(user));
        log.trace("signUp - method finished result={}",result);

        return userDto;
    }
}
