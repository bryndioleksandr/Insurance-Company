package org.company.insurance.dto;

import org.company.insurance.enums.InsuranceType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.InsurancePolicy}
 */
public record InsurancePolicyDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String policyNumber,
                                 Date startDate, Date endDate, double price,
                                 InsuranceType insuranceType) implements Serializable {
}