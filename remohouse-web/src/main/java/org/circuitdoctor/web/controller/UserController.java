package org.circuitdoctor.web.controller;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.service.UserService;
import org.circuitdoctor.web.converter.UserConverter;
import org.circuitdoctor.web.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserConverter userConverter;

    @RequestMapping(value = "user/login", method = RequestMethod.PUT)
    boolean login(@RequestBody UserDto userDto){
        //send the user's ID from db!!
        log.trace("login - method entered user={}",userDto);
        User user = userConverter.convertDtoToModel(userDto);
        AtomicBoolean result = new AtomicBoolean(false);
        result.set(userService.login(user));
        log.trace("login - method finished result={}",result.get());
        return result.get();
    }
    @RequestMapping(value = "user/signUp", method = RequestMethod.POST)
    UserDto signUp(@RequestBody @Valid UserDto userDto, Errors errors){
        //return null if some error occurred
        log.trace("signUp - method entered user={}",userDto);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error-> log.error("error - {}",error.toString()));
            log.trace("signUp - validation error");
            return null;
        }
        User user = userConverter.convertDtoToModel(userDto);
        UserDto result= userConverter.convertModelToDto(userService.signUp(user));
        log.trace("signUp - method finished result={}",result);
        return userDto;
    }
    @RequestMapping(value = "user/changePassword", method = RequestMethod.PUT)
    public UserDto changePassword(@RequestBody @Valid UserDto userDto,Errors errors){
        log.trace("changePassword - method entered user={}",userDto);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error-> log.trace("error - {}",error.toString()));
            log.trace("changePassword - validation error");
            return null;
        }
        User user=userConverter.convertDtoToModel(userDto);
        User newUser = userService.changePassword(user);
        UserDto newUserDto = null;
        if(newUser!=null)
            newUserDto =userConverter.convertModelToDto(newUser);

        log.trace("changePassword - method finished user={}",newUserDto);
        return newUserDto;
    }


}