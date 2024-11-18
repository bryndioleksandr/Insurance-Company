package org.company.insurance.exception;

import jakarta.persistence.EntityExistsException;

public class PolicyHolderAlreadyExistsException extends EntityExistsException {
    public PolicyHolderAlreadyExistsException(String message) {
        super(message);
    }
}
