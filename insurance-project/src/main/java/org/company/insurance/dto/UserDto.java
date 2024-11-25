package org.company.insurance.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
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
                      @Email
                      @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email should be valid")
                      @Size(max = 255)
                      String email,
                      @Size(max = 255)
                      String firstName,
                      @Size(max = 255)
                      String surname,
                      String username,
                      @Size(max = 21)
                      @Pattern(regexp = "^\\+\\d{1,4}\\d{7,15}$", message = "Phone number must be in the format +<country code><phone number>")
                      String phoneNumber,
                      @Past
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