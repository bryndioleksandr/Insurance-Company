package org.company.insurance.exception;

import jakarta.persistence.EntityExistsException;


public class AgentAlreadyExistsException extends EntityExistsException {
    public AgentAlreadyExistsException(String message) {
        super(message);
    }
}
