package org.company.insurance.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link org.company.insurance.entity.PropertyInsurance}
 */
public record PropertyInsuranceCreationDto(double coverageAmount, int insuranceLongevity,
                                           @NotBlank String propertyAddress, Long insurancePolicyId) implements Serializable {
}