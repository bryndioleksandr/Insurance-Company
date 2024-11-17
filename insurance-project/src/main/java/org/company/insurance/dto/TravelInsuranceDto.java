package org.company.insurance.dto;

import org.company.insurance.enums.CoverageArea;
import org.company.insurance.enums.TravelType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.company.insurance.entity.TravelInsurance}
 */
public record TravelInsuranceDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, double coverageAmount,
                                 int insuranceLongevity, Long insurancePolicyId, String insurancePolicyPolicyNumber,
                                 CoverageArea coverageArea, String destination, TravelType travelType) implements Serializable {
}