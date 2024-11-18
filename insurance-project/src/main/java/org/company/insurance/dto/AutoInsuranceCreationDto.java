package org.company.insurance.dto;

import org.company.insurance.enums.AutoInsuranceType;
import org.company.insurance.enums.VehicleType;

import java.io.Serializable;

/**
 * DTO for {@link org.company.insurance.entity.AutoInsurance}
 */
public record AutoInsuranceCreationDto(double coverageAmount, double engineCapacity, int insuranceLongevity, String brand, String model,
                                       int year, String plate, VehicleType type,
                                       AutoInsuranceType insuranceType, Long insurancePolicyId) implements Serializable {
}