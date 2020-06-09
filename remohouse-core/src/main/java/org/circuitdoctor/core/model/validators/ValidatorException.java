package org.circuitdoctor.core.model.validators;

import org.springframework.stereotype.Component;

@Component
public class ValidatorException extends RuntimeException {
    public ValidatorException(String message) {
        super(message);
    }
    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }
    public ValidatorException(Throwable cause) {
        super(cause);
    }

    public ValidatorException() {
        super("Entity is null");
    }
}