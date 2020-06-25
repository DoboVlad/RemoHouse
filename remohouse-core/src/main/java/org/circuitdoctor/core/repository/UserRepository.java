package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.User;

import java.util.Optional;

public interface UserRepository extends Repository<User,Long> {
    Optional<User> findAllByEmail(String email);
    Optional<User> findAllByEmailStartsWith(String email);
    Optional<User> findAllByPhoneNumber(String phoneno);
}