package org.circuitdoctor.core.service;
import java.io.IOException;
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
        /*
        DESCR:gets all the users from the database
        PARAM:-
        PRE:-
        POST:-
         */
        log.trace("getAllUsers - method entered");
        List<User> result=userRepository.findAll();
        log.trace("getAllUsers - method finished result={}",result);
        return result;
    }

    @Override
    public boolean login(User user) {
        /*
        DESCR:checks if the user given corresponds with any of the users from the database
        PARAM:user - User. Compulsory fields: email or phone number, id, password. The rest must not be null.
        PRE:user not null
        POST:return true if the password corresponds to the email/phone number
                    false otherwise
         */
        log.trace("login - method entered user={}",user);
        String phoneNo = user.getPhoneNumber();
        String password = user.getPassword();
        String email = user.getEmail();
        AtomicBoolean result = new AtomicBoolean(false);
        Optional<User> userFromDB = userRepository.findAllByEmail(user.getEmail());
        log.trace("email");
        userFromDB.ifPresent(userDB->{
            log.trace(userDB.toString());
            if((userDB.isValidated() && (userDB.getPhoneNumber().equals(phoneNo) || userDB.getEmail().equals(email)) && userDB.getPassword().equals(password)))
                result.set(true);
        });
        if(!result.get()){
            log.trace("phone number");
            userFromDB = userRepository.findAllByPhoneNumber(user.getPhoneNumber());
            userFromDB.ifPresent(userDB->{
                log.trace(userDB.toString());
                if((userDB.isValidated() && (userDB.getPhoneNumber().equals(phoneNo) || userDB.getEmail().equals(email)) && userDB.getPassword().equals(password)))
                    result.set(true);
            });
        }
        log.trace("login - method finished r={}",result.get());
        return result.get();
    }

    @Override
    public User signUp(@Valid User user) {
        /*
        DESCR:adds a new user to the database
        PARAM:user - User
        PRE:user must be valid
        POST:returns the user that was saved in the database. The id is updated with respect to the rule in BaseEntity
         */
        log.trace("signUp - method entered user={}",user);
        User newUser=userRepository.save(user);

        log.trace("signUp - method finished newUser={}",newUser);
        return newUser;
    }

    @Override
    public User changePassword(User user) {
        /*
        DESCR:updates an user. Only the password has to be updated.
        PARAM:user - User
        PRE:user not null
        POST: returns the user saved if everything went well
                      null if the password is not valid or if the user has other fields modified.
         */
        log.trace("changePassword - method entered user={}",user);

        AtomicReference<User> newUser = new AtomicReference<>();
        Optional<User> userFromDB = userRepository.findById(user.getId());
        if(userFromDB.get().getPassword().length()<7){
            log.trace("changePassword - invalid Password size(<7)");
            return userFromDB.get();

        }

        userFromDB.ifPresent(userDB->{
            userDB.setPassword(user.getPassword());
            userRepository.save(userDB);
            newUser.set(userDB);
        });

        log.trace("changePassword - method finished user={}",newUser);
        return newUser.get();

    }

    @Override
    public Optional<User> getUserByCredential(String credential) {
        /*
        DESCR:gets the user with the credentials (=email or phone number) given
        PARAM:credential - string
        PRE:credential not null
        POST:return Optional<User> which can be null only if no user with these credentials was found
         */
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
        /*
        DESCR:generates a code that will be send via email to the given email
        PARAM:email - string
        PRE:one of the existing users has this email
        POST:returns the generated code if an user with this email exists
                     "" otherwise
         */
        log.trace("recover password email -method entered email={}",email);
        Optional<User> user=userRepository.findAllByEmail(email);
        AtomicReference<String> result= new AtomicReference<>("incorrect email");
        user.ifPresent(u->{
            //String to = email;
            String generatedCode=generateRandomString();
            // Sender's email ID needs to be mentioned
            String from = "remo@circuitdoctor.ro";
            String password="ParolaRemo123";
            // Assuming you are sending email from localhost
            String message="Recover password code: "+generatedCode;
            String subject="Recover password REMO";
            sendEmail(from,email,password,message,subject);
            log.trace("recover password email -method finished code={}",generatedCode);
            result.set(generatedCode);
        });
        return result.get();

    }

    @Override
    public String recoverPasswordByMessage(String phoneNumber) {
        /*
        DESCR:generates a code that will be sent via SMS to the given phone number
        PARAM:phoneNumber - string
        PRE:one of the existing users has this phone number
        POST:returns the generated code if a user with this phone number exists
                     "" otherwise
         */
        log.trace("recover password by message -method entered pn={}",phoneNumber);
        Optional<User> user=userRepository.findAllByPhoneNumber(phoneNumber);
        AtomicReference<String> result= new AtomicReference<>("incorrect phone number");
        user.ifPresent(u->{
            String generatedCode=generateRandomString();
            ServiceUtils utils=new ServiceUtils();
            String res=utils.sendMessage("REMO change password code: "+generatedCode,phoneNumber);

            log.trace("recover password by message -method finished code={}",generatedCode);

            if(res.equals("ok"))
                result.set(generatedCode);
            else {
                result.set("something went wrong when tried to send message");
            }
        });

        return result.get() ;
    }

    @Override
    public String confirmEmail(String email) {
        /*
        DESCR:generates a code that will be sent via email to the given email
        PARAM:email - string
        PRE:one of the existing users has this email
        POST:returns the generated code if a user with this email exists
                     "" otherwise
         */
        log.trace("cofirmEmail -method entered email={}",email);
        Optional<User> user=userRepository.findAllByEmail(email);
        AtomicReference<String> result= new AtomicReference<>("incorrect email");
        user.ifPresent(u->{
            //String to = email;
            String generatedCode=generateRandomString();
            // Sender's email ID needs to be mentioned
            String from = "remo@circuitdoctor.ro";
            String password="ParolaRemo123";
            // Assuming you are sending email from localhost
            String message="Confirmation code: "+generatedCode;
            String subject="Confirm account REMO";
            sendEmail(from,email,password,message,subject);
            log.trace("confirmEmail -method finished code={}",generatedCode);
            result.set(generatedCode);
        });
        return result.get();
    }


    static void sendEmail(String from,String to,String password,String emailMessage,String subject){
        /*
        DESCR:sends an email from an email to another one
        PARAM:from         - string: the email address from which the email is sent
              to           - string: the email address to which the email is sent
              password     - string: the password of the {from} email
              emailMessage - string : the content of the email
              subject      - string: the subject of the email
        PRE:none of the parameters are null.
            {password} is the correct password for {from}
            {from} and {to} are existing emails
        POST:-
         */
        Properties props = System.getProperties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "mail.circuitdoctor.ro");
        props.put("mail.smtp.starttls.enable", "true");
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
            transport.connect("mail.circuitdoctor.ro",26, from, password);//CAUSES EXCEPTION
            transport.sendMessage(message,message.getAllRecipients());
            log.trace("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    String generateRandomString(){
        /*
        DESCR:function that generates a random string of 6 letters
        PARAM:-
        PRE:-
        POST:returns the generated string
         */
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


    @Override
    public void sendEmailWithActionLogs(Long userId,String extension,String startDate,String endDate,boolean takeAll) {
        log.trace("entered sendEmailActonLogs user={}",userId);
        String from = "remo@circuitdoctor.ro";
        String password="ParolaRemo123";
        // Assuming you are sending email from localhost
        String message="Action Logs";
        String subject="Action Logs";
        Optional<User> userFromDB = userRepository.findById(userId);
        String filename="logFile."+extension;
        userFromDB.ifPresent(user->{
            ServiceUtils utils=new ServiceUtils();
            try {
                if(takeAll)
                    utils.writeToFile(actionLogGSMService.findAllActions(userId),filename);
                else
                    utils.writeToFile(actionLogGSMService.findAllActionsBeetwenDates(userId,startDate,endDate),filename);
                utils.sendEmailWithAttachment(from,user.getEmail(),password,message,subject,filename);
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        });
        log.trace("method finished - sendEmailActionLog");
    }

    @Override
    public void sendEmailWithActionLogsFromGSMs(Long userId, String extension, List<Long> gsmIds, String startDate, String endDate) {
        log.trace("entered sendEmailActonLogsFromGSMs user={}",userId);
        String from = "remo@circuitdoctor.ro";
        String password="ParolaRemo123";
        // Assuming you are sending email from localhost
        String message="Action Logs";
        String subject="Action Logs";
        Optional<User> userFromDB = userRepository.findById(userId);
        String filename="logFile."+extension;
        userFromDB.ifPresent(user->{
            ServiceUtils utils=new ServiceUtils();
            try {
                    utils.writeToFile(actionLogGSMService.findAllActionsFromGSMsBeetwenDates(userId,gsmIds,startDate,endDate),filename);
                utils.sendEmailWithAttachment(from,user.getEmail(),password,message,subject,filename);
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        });
        log.trace("method finished - sendEmailActionLogFromGSMs");
    }

}
