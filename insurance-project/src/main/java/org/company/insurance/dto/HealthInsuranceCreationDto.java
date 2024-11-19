package org.company.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link org.company.insurance.entity.HealthInsurance}
 */
public record HealthInsuranceCreationDto(double coverageAmount,
//                                         int insuranceLongevity,
                                         @NotBlank @Size(max = 512) String medicalHistory,
                                         Long insurancePolicyId) implements Serializable {
}