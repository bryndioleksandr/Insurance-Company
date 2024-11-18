package org.company.insurance.exception;

import jakarta.persistence.EntityNotFoundException;

public class InsurancePolicyNotFoundException extends EntityNotFoundException {
    public InsurancePolicyNotFoundException(String message) {
        super(message);
    }
}
