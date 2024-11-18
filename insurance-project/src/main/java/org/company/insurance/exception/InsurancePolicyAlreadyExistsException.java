package org.company.insurance.exception;

import jakarta.persistence.EntityExistsException;

public class InsurancePolicyAlreadyExistsException extends EntityExistsException {
    public InsurancePolicyAlreadyExistsException(String message) {
        super(message);
    }
}
