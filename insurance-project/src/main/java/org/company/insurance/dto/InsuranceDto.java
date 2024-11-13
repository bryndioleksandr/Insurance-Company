package org.company.insurance.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.company.insurance.entity.Insurance}
 */
public record InsuranceDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, double coverageAmount,
                           int insuranceLongevity, Long insurancePolicyId,
                           String insurancePolicyPolicyNumber) implements Serializable {
}