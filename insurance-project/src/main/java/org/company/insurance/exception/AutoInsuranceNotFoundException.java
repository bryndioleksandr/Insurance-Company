package org.company.insurance.exception;

import jakarta.persistence.EntityNotFoundException;

public class AutoInsuranceNotFoundException extends EntityNotFoundException {
    public AutoInsuranceNotFoundException(String message) {
        super(message);
    }
}
