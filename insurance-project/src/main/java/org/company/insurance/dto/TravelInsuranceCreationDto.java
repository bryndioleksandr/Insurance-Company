package org.company.insurance.dto;

import org.company.insurance.enums.CoverageArea;
import org.company.insurance.enums.TravelType;

import java.io.Serializable;

/**
 * DTO for {@link org.company.insurance.entity.TravelInsurance}
 */
public record TravelInsuranceCreationDto(double coverageAmount, int insuranceLongevity, CoverageArea coverageArea, String destination,
                                         TravelType travelType, Long insurancePolicyId) implements Serializable {
}