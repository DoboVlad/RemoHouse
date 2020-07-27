package org.circuitdoctor.web.controller;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.util.IOUtils;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.UserRepository;
import org.circuitdoctor.core.service.UserService;
import org.circuitdoctor.web.converter.UserConverter;
import org.circuitdoctor.web.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "user/login", method = RequestMethod.PUT)
    boolean login(@RequestBody UserDto userDto){
        /*
        DESCR:
        PARAM:
        PRE:
        POST
         */
        //send the user's ID from db!!
        log.trace("login - method entered user={}",userDto);
        User user = userConverter.convertDtoToModel(userDto);
        AtomicBoolean result = new AtomicBoolean(false);
        result.set(userService.login(user));
        log.trace("login - method finished result={}",result.get());
        return result.get();
    }
    @RequestMapping(value = "user/signUp", method = RequestMethod.POST)
    String signUp(@RequestBody @Valid UserDto userDto, BindingResult errors){
        /*
        DESCR:
        PARAM:
        PRE:
        POST
         */
        //return null if some error occurred
        log.trace("signUp - method entered user={}",userDto);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error-> log.error("error - {}",error.toString()));
            log.trace("signUp - validation error");
            return "validation errors";
        }
        User user = userConverter.convertDtoToModel(userDto);
        UserDto result= userConverter.convertModelToDto(userService.signUp(user));
        log.trace("signUp - method finished result={}",result);
        return String.valueOf(userDto.getId());
    }

    @RequestMapping(value = "user/changePassword/{userID}", method = RequestMethod.PUT)
    public String changePassword(@RequestBody @Valid UserDto userDto, @PathVariable Long userID, BindingResult errors){
        /*
        DESCR:
        PARAM:
        PRE:
        POST
         */
        log.trace("changePassword - method entered user={}",userDto);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error-> log.trace("error - {}",error.toString()));
            log.trace("changePassword - validation error");
            return "validation errors";
        }

        if(userID.equals(userDto.getId())) {
            User user = userConverter.convertDtoToModel(userDto);
            User newUser = userService.changePassword(user);
            UserDto newUserDto = null;
            if (newUser != null)
                newUserDto = userConverter.convertModelToDto(newUser);


            log.trace("changePassword - method finished user={}", newUserDto);
            if (newUser != null)
                return String.valueOf(newUser.getId());
            return "null";
        }
        log.warn("changePassword - user ids don't match");
        return "user ids don't match";
    }

    @RequestMapping(value = "user/getUserByCredential/{credential}", method = RequestMethod.GET)
    public UserDto getUserByCredential(@PathVariable String credential){
        /*
        DESCR:
        PARAM:
        PRE:
        POST
         */
        log.trace("getUserByCredential - method entered c={}",credential);
        Optional<User> result = userService.getUserByCredential(credential);
        log.trace("getUserByCredential - method finished r={}",result);
        AtomicBoolean userExists = new AtomicBoolean(false);
        result.ifPresent(user-> userExists.set(true));
        if(!userExists.get())
            return new UserDto();
        return userConverter.convertModelToDto(result.get());
    }
    @RequestMapping(value = "user/recoverPassword/{credential}", method = RequestMethod.GET)
    public String recoverPassword(@PathVariable String credential){
        /*
        DESCR:
        PARAM:
        PRE:
        POST
         */
        log.trace("recoverPassword - method entered email={}",credential);
        String code="";
        if(credential.contains("@")){
            code= userService.recoverPasswordByEmail(userService.getUserByCredential(credential).get().getEmail());
        }
        else{
            if(credential.length()!=10){
                return "wrong phoneNumber";
            }
            code= userService.recoverPasswordByMessage(credential);
            if(code.length()>6){
                return "something went wrong when tried to send the message";
            }
        }
        log.trace("recoverPassword - method finished code={}",code);
        return code;
    }

    @RequestMapping(value = "user/confirmEmail/{email}", method = RequestMethod.GET)
    public String confirmEmail(@PathVariable String email){
        /*
        DESCR:
        PARAM:
        PRE:
        POST
         */
        log.trace("confirmEmail - method entered email={}",email);
        String code="";
        code= userService.confirmEmail(userService.getUserByCredential(email).get().getEmail());
        log.trace("recoverPassword - method finished code={}",code);
        return code;
    }

    @RequestMapping(value = "user/validateAccount/{email}", method = RequestMethod.GET)
    public void validateAccount(@PathVariable String email){
        /*
        DESCR:validates an account with the given email
        PARAM:email - string
        PRE:email not null
        POST:-
         */
        log.trace("validateAccount - method entered email={}",email);
        Optional<User> user = userService.getUserByCredential(email);
        user.ifPresent(u->{
            u.setValidated(true);
            userRepository.save(u);
        });
        log.trace("validateAccount - method finished");
    }

    @RequestMapping(value = "user/sendEmailActions/{userID}/{ext}/{startDate}/{endDate}/{takeAll}",method = RequestMethod.PUT)
    String sendEmailWithActions(@PathVariable Long userID,@PathVariable String ext,@PathVariable String startDate,
                                @PathVariable String endDate,@PathVariable boolean takeAll){

        log.trace("getActions - method entered userID={}",userID);
        if(ext.equals("csv") || ext.equals("txt") || ext.equals("pdf")){
            userService.sendEmailWithActionLogs(userID,ext,startDate,endDate,takeAll);
            log.trace("getActions - method finished");
            return "email sent";
        }

        return "the extension must be txt,csv or pdf";
    }
    @RequestMapping(value = "user/sendEmailActionsFromGSMs/{userID}/{ext}/{startDate}/{endDate}/{takeAll}",method = RequestMethod.PUT)
    String sendEmailWithActionsFromGSMs(@RequestBody List<Long> gsmIds, @PathVariable Long userID, @PathVariable String ext, @PathVariable String startDate,
                                        @PathVariable String endDate,@PathVariable boolean takeAll){
        System.out.println(takeAll);
        log.trace("getActionsFromGSMs - method entered userID={}",userID);
        System.out.println(gsmIds);
        //System.out.println(gsmIds);
        if(ext.equals("csv") || ext.equals("txt") || ext.equals("pdf")){
            userService.sendEmailWithActionLogsFromGSMs(userID,ext,gsmIds,startDate,endDate,takeAll);
            log.trace("getActionsFromGSMs - method finished");
            return "email sent";
        }

        return "the extension must be txt, csv or pdf";
    }

    @RequestMapping(value = "user/checkPassword/{userID}",method = RequestMethod.PUT)
    boolean verifyPasword(@RequestBody String password, @PathVariable Long userID){
        log.trace("verifyPassword - method entered u={}",userID);
        boolean ok = this.userService.verifyPassword(userID,password);
        log.trace("verifyPassword - method finished r={}",ok);
        return ok;
    }

    @RequestMapping(value="user/downloadLogFile/{userID}/{ext}/{startDate}/{endDate}/{takeAll}",method = RequestMethod.GET,
    produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getLogFile(HttpSession session, HttpServletResponse response, @RequestBody List<Long> gsmIds,
                                             @PathVariable Long userID, @PathVariable String ext, @PathVariable String startDate,
                                             @PathVariable String endDate, @PathVariable boolean takeAll) throws Exception {
        try {
            if(ext.equals("csv") || ext.equals("txt")){
                userService.setFileToBeDownloaded(userID,ext,gsmIds,startDate,endDate,takeAll);
                String filename="logFile."+ext;
                FileSystemResource fileResource = new FileSystemResource(filename);

                byte[] base64Bytes = Base64.encodeBase64(IOUtils.toByteArray(fileResource.getInputStream()));

                HttpHeaders headers = new HttpHeaders();
                headers.add(filename, fileResource.getFilename());

                return ResponseEntity.ok().headers(headers).body(base64Bytes);
            }
            throw new Exception("extension must  be txt or csv");


        } catch (Exception e){

            e.printStackTrace();
        }
        return null;
    }

}
