package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public interface UserRepository extends Repository<User,Long> {
    Optional<User> findAllByEmail(String email);
    Optional<User> findAllByEmailStartsWith(String email);
    Optional<User> findAllByPhoneNumber(String phoneno);
    Optional<User> findById(Long id);

}