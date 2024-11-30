package org.company.insurance.dto;

import org.company.insurance.enums.InsuranceType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link org.company.insurance.entity.InsurancePolicy}
 */
public record InsurancePolicyDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String policyNumber,
                                 Long userId, LocalDate startDate, LocalDate endDate, Double price, InsuranceType insuranceType,
                                 List<Long> claimIds, Long policyHolderId) implements Serializable {
}