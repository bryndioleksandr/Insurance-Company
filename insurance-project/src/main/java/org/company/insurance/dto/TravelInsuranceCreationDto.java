package org.company.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import org.company.insurance.enums.CoverageArea;
import org.company.insurance.enums.TravelType;

import java.io.Serializable;

/**
 * DTO for {@link org.company.insurance.entity.TravelInsurance}
 */
public record TravelInsuranceCreationDto(Double coverageAmount,
                                         @NotBlank CoverageArea coverageArea,
                                         @NotBlank String destination,
                                         @NotBlank TravelType travelType,
                                         Long insurancePolicyId) implements Serializable {
}