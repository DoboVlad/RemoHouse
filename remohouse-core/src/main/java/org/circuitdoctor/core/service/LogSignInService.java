package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.LogSignIn;
import org.circuitdoctor.core.model.User;

import java.util.Set;

public interface LogSignInService {
    Set<LogSignIn> getLogs(User user);
    LogSignIn addLog(LogSignIn logSignIn);
}
