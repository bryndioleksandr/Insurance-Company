package org.company.insurance.exception;

import jakarta.persistence.EntityExistsException;


public class AutoInsuranceAlreadyExistsException extends EntityExistsException {
    public AutoInsuranceAlreadyExistsException(String message) {
        super(message);
    }
}
