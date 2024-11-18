package org.company.insurance.exception;

import jakarta.persistence.EntityNotFoundException;

public class ClaimAssessmentNotFoundException extends EntityNotFoundException {
    public ClaimAssessmentNotFoundException(String message) {
        super(message);
    }
}
