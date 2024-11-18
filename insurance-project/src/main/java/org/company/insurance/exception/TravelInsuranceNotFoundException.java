package org.company.insurance.exception;

import jakarta.persistence.EntityNotFoundException;

public class TravelInsuranceNotFoundException extends EntityNotFoundException {
    public TravelInsuranceNotFoundException(String message) {
        super(message);
    }
}
