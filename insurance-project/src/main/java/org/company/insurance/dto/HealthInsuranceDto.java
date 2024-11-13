package org.company.insurance.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.company.insurance.entity.HealthInsurance}
 */
public record HealthInsuranceDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, double coverageAmount,
                                 int insuranceLongevity, Long insurancePolicyId, String insurancePolicyPolicyNumber,
                                 String medicalHistory) implements Serializable {
}