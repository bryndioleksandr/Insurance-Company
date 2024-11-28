package org.company.insurance.dto;

import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link org.company.insurance.entity.PolicyHolder}
 */
public record PolicyHolderDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String email, String firstName,
                              String surname, String phoneNumber, Date birthDate, @Pattern(regexp = "^\\d{9}$") String passportNumber, String address,
                              List<InsurancePolicyDto1> insurancePolicies) implements Serializable {
    /**
     * DTO for {@link org.company.insurance.entity.InsurancePolicy}
     */
    public record InsurancePolicyDto1(Long id, String policyNumber) implements Serializable {
    }
}