package org.company.insurance.dto;

import java.io.Serializable;

/**
 * DTO for {@link org.company.insurance.entity.HealthInsurance}
 */
public record HealthInsuranceCreationDto(double coverageAmount, int insuranceLongevity,
                                         String medicalHistory) implements Serializable {
}