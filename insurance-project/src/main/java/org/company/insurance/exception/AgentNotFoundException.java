package org.company.insurance.exception;

import jakarta.persistence.EntityNotFoundException;

public class AgentNotFoundException extends EntityNotFoundException {
    public AgentNotFoundException(String message) {
        super(message);
    }
}
