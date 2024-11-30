package org.company.insurance.dto;

import org.company.insurance.enums.InsuranceType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.company.insurance.entity.InsurancePolicy}
 */
public record InsurancePolicyCreationDto_(LocalDateTime createdAt, LocalDateTime updatedAt, String policyNumber,
                                          Long userId, LocalDate startDate, LocalDate endDate, Double price,
                                          InsuranceType insuranceType, Long policyHolderId, Long autoInsuranceId,
                                          Long travelInsuranceId, Long healthInsuranceId,
                                          Long propertyInsuranceId) implements Serializable {
}