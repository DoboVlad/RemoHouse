package org.circuitdoctor.core.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
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

    @Override
    public String recoverPasswordByEmail(String email) {
        log.trace("recover password email -method entered email={}",email);
        String to = email;
        String generatedCode=generateRandomString();

        String from = "andrei.bangau99@gmail.com";
        String password= "cgqkzy@A";

        String host = "localhost";
        String message="Recover password code: "+generatedCode;
        String subject="Recover password REMO";
        sendEmail(host,from,to,hashPassword(password),message,subject);

        log.trace("recover password email -method finished code={}",generatedCode);

        return generatedCode;
    }

    @Override
    public String recoverPasswordByMessage(String phoneNumber) {
        log.trace("recover password by message -method entered pn={}",phoneNumber);
        String generatedCode=generateRandomString();
        ServiceUtils utils=new ServiceUtils();
        String result=utils.sendMessage("REMO change password code: "+generatedCode,"0759021544");

        log.trace("recover password by message -method finished code={}",generatedCode);

        if(result.equals("ok"))
            return generatedCode;
        return "something went wrong when tried to send message";
    }


    static void sendEmail(String host,String from,String to,String password,String emailMessage,String subject){
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);

        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        // Get the default Session object.


        try {
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipients(Message.RecipientType.TO, to);
            message.setSubject(subject);
            message.setText(emailMessage);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", from, password);//CAUSES EXCEPTION
            transport.sendMessage(message,message.getAllRecipients());
            log.trace("sendEmail-method: Sent message successfully...");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    String generateRandomString(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
    String hashPassword(String pass){
        char[] result = new char[pass.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (char) (pass.charAt(i) - (i+1));
        }
        return new String(result);
    }





}
