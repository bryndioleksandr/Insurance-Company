package org.company.insurance.dto;

import org.company.insurance.enums.AutoInsuranceType;
import org.company.insurance.enums.VehicleType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.company.insurance.entity.AutoInsurance}
 */
public record AutoInsuranceDto1(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Double coverageAmount,
                                int insuranceLongevity, String brand, String model, int year, String plate,
                                VehicleType type, AutoInsuranceType insuranceType, Long insurancePolicyId,
                                String insurancePolicyPolicyNumber) implements Serializable {
}