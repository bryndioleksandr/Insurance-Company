package org.company.insurance.dto;

import org.company.insurance.enums.InsuranceType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.InsurancePolicy}
 */
public record InsurancePolicyCreationDto(LocalDateTime createdAt, LocalDateTime updatedAt, String policyNumber,
                                         Long userId, LocalDate startDate, LocalDate endDate, double price,
                                         InsuranceType insuranceType, Long policyHolderId) implements Serializable {
}