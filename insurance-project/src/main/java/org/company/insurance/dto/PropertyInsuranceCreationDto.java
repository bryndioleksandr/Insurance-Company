package org.company.insurance.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link org.company.insurance.entity.PropertyInsurance}
 */
public record PropertyInsuranceCreationDto(Double coverageAmount,
                                           @NotBlank String propertyAddress,
                                           @NotNull @Positive @Max(10000) @Min(15) Double houseSize,
                                           Long insurancePolicyId) implements Serializable {
}