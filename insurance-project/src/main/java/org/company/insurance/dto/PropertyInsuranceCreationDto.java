package org.company.insurance.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link org.company.insurance.entity.PropertyInsurance}
 */
public record PropertyInsuranceCreationDto(double coverageAmount,
                                           @NotBlank String propertyAddress,
                                           @NotNull @Positive @Max(10000) @Min(15) double houseSize,
                                           Long insurancePolicyId) implements Serializable {
}