package org.company.insurance.dto;

import jakarta.annotation.Nullable;
import org.company.insurance.enums.Role;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link org.company.insurance.entity.User}
 */
public record UserDto(Long id,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      String email,
                      String firstName,
                      String surname,
                      String username,
                      String phoneNumber,
                      LocalDate birthDate,
                      String password,
                      Role role
        /*@Nullable LocalDate hireDate,
        @Nullable String position*/) implements Serializable {
    /**
     * DTO for {@link org.company.insurance.entity.InsurancePolicy}
     */
    public record InsurancePolicyDto1(Long id, String policyNumber) implements Serializable {
    }
}