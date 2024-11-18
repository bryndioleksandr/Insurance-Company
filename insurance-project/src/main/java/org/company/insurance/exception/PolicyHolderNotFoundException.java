package org.company.insurance.exception;

import jakarta.persistence.EntityNotFoundException;

public class PolicyHolderNotFoundException extends EntityNotFoundException {
    public PolicyHolderNotFoundException(String message) {
        super(message);
    }
}
