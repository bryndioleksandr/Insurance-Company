package org.company.insurance.dto;

import org.company.insurance.enums.AutoInsuranceType;
import org.company.insurance.enums.VehicleType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.company.insurance.entity.AutoInsurance}
 */
public record AutoInsuranceDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, double coverageAmount,
                               int insuranceLongevity, String brand, String model, int year, String plate,
                               VehicleType type, AutoInsuranceType insuranceType) implements Serializable {
}