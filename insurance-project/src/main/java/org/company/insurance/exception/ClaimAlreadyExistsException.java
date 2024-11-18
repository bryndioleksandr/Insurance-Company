package org.company.insurance.exception;

import jakarta.persistence.EntityExistsException;

public class ClaimAlreadyExistsException extends EntityExistsException {
    public ClaimAlreadyExistsException(String message) {
        super(message);
    }
}
