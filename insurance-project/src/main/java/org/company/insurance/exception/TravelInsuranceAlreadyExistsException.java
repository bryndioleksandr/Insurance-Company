package org.company.insurance.exception;

import jakarta.persistence.EntityExistsException;

public class TravelInsuranceAlreadyExistsException extends EntityExistsException {
    public TravelInsuranceAlreadyExistsException(String message) {
        super(message);
    }
}
