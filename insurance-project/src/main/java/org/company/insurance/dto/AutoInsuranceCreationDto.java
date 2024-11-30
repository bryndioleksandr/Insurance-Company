package org.company.insurance.dto;

import jakarta.validation.constraints.*;
import org.company.insurance.enums.AutoInsuranceType;
import org.company.insurance.enums.VehicleType;

import java.io.Serializable;

/**
 * DTO for {@link org.company.insurance.entity.AutoInsurance}
 */
public record AutoInsuranceCreationDto( Double coverageAmount,
                                       @NotBlank @Min(50) @Max(10000) Double engineCapacity,
//                                       int insuranceLongevity,
                                       @NotBlank @Size(max = 255) String brand,
                                       @NotBlank @Size(max = 255) String model,
                                        @PastOrPresent @Min(1900) @Size(max = 4) int year,
                                       @NotBlank @Size(max = 8) String plate,
                                       VehicleType type,
                                       AutoInsuranceType insuranceType,
                                       Long insurancePolicyId) implements Serializable {
}