package org.company.insurance.exception;

import jakarta.persistence.EntityExistsException;

public class PropertyInsuranceAlreadyExistsException extends EntityExistsException {
    public PropertyInsuranceAlreadyExistsException(String message) {
        super(message);
    }
}
