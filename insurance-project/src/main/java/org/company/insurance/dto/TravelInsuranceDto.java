package org.company.insurance.dto;

import org.company.insurance.enums.TravelType;
import org.company.insurance.entity.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.company.insurance.entity.TravelInsurance}
 */
public record TravelInsuranceDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, double coverageAmount, int insuranceLongevity, String destination, TravelType travelType) implements Serializable {
  }