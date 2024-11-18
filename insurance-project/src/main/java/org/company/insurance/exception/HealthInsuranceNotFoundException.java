package org.company.insurance.exception;

import jakarta.persistence.EntityNotFoundException;

public class HealthInsuranceNotFoundException extends EntityNotFoundException {
    public HealthInsuranceNotFoundException(String message) {
        super(message);
    }
}
