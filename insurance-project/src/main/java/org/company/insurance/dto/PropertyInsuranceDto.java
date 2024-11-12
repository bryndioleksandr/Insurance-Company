package org.company.insurance.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.company.insurance.entity.PropertyInsurance}
 */
public record PropertyInsuranceDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, double coverageAmount, int insuranceLongevity, String propertyAddress) implements Serializable {
  }