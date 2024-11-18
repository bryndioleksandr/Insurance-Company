package org.company.insurance.exception;

import jakarta.persistence.EntityNotFoundException;

public class PropertyInsuranceNotFoundException extends EntityNotFoundException {
    public PropertyInsuranceNotFoundException(String message) {
        super(message);
    }
}
