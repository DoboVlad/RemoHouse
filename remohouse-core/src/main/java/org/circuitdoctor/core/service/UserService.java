package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    boolean login(User user);
    User signUp(User user);
    User changePassword(User user);


}
