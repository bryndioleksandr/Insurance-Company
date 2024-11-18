package org.company.insurance.exception;

import jakarta.persistence.EntityNotFoundException;

public class ClaimNotFoundException extends EntityNotFoundException {
    public ClaimNotFoundException(String message) {
        super(message);
    }
}
