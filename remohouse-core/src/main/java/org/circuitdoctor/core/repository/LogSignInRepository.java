package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.LogSignIn;
import org.circuitdoctor.core.model.User;
import org.springframework.stereotype.Component;
import java.util.Set;
@Component
public interface LogSignInRepository extends Repository<LogSignIn,Long>  {
    Set<LogSignIn> findAllByUser(User user);
}
