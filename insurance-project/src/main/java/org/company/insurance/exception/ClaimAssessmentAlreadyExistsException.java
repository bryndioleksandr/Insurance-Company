package org.company.insurance.exception;

import jakarta.persistence.EntityExistsException;

public class ClaimAssessmentAlreadyExistsException extends EntityExistsException {
    public ClaimAssessmentAlreadyExistsException(String message) {
        super(message);
    }
}
