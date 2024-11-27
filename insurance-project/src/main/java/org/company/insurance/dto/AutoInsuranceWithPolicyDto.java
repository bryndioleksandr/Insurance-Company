package org.company.insurance.dto;

import org.company.insurance.enums.AutoInsuranceType;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.InsuranceType;
import org.company.insurance.enums.VehicleType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.company.insurance.entity.AutoInsurance}
 */
public record AutoInsuranceWithPolicyDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
                                         double coverageAmount, int insuranceLongevity, double engineCapacity,
                                         String brand, String model, int year, String plate, VehicleType type,
                                         AutoInsuranceType insuranceType, Long insurancePolicyId,
                                         String insurancePolicyPolicyNumber, Long insurancePolicyUserId,
                                         LocalDate insurancePolicyStartDate, LocalDate insurancePolicyEndDate,
                                         double insurancePolicyPrice, InsuranceStatus insurancePolicyStatus,
                                         InsuranceType insurancePolicyInsuranceType,
                                         Long insurancePolicyPolicyHolderId) implements Serializable {
}