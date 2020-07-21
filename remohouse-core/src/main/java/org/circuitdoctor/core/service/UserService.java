package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    boolean login(User user);
    User signUp(User user);
    User changePassword(User user);
    Optional<User> getUserByCredential(String credential);
    String recoverPasswordByEmail(String email);
    String recoverPasswordByMessage(String phoneNumber);

    void setFileToBeDownloaded(Long userId, String extension,List<Long> gsmIds, String startDate, String endDate, boolean takeAll);

    void sendEmailWithActionLogs(Long userId, String extension, String startDate, String endDate, boolean takeAll);
    void sendEmailWithActionLogsFromGSMs(Long userId,String extension,List<Long> gsmIds,String startDate,String endDate,boolean takeAll);


    String confirmEmail(String email);

    boolean verifyPassword(Long userID, String password);
}
