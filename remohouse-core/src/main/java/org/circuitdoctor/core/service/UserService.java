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


}
