package org.company.insurance.exception;

import jakarta.persistence.EntityExistsException;

public class HealthInsuranceAlreadyExistsException extends EntityExistsException {
    public HealthInsuranceAlreadyExistsException(String message) {
        super(message);
    }
}
