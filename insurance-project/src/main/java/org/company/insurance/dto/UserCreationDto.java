package org.company.insurance.dto;

import jakarta.validation.constraints.*;
import org.company.insurance.enums.Role;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.User}
 */

public record UserCreationDto(
                              @Email
                              @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email should be valid")
                              @NotBlank
                              @Size(max = 255)
                              String email,
                              String username,
                              @NotBlank @Size(max = 255) String firstName,
                              @NotBlank @Size(max = 255) String surname,
                              @NotBlank @Size(max = 21)
                              @Pattern(regexp = "^\\+\\d{1,4}\\d{7,15}$", message = "Phone number must be in the format +<country code><phone number>")
                              String phoneNumber,
                              @Past
                              LocalDate birthDate,
                              String password,
                              Role role
//                              String emailVerificationCode,
//                              @Value("#{T(java.time.LocalDateTime).now().plusMinutes(15)}")
//                              LocalDateTime emailVerificationExpiry,
//                              @Value("false")
//                              boolean emailVerified
//                              @Past
//                              LocalDate hireDate,
//                              @Size(max = 255)
//                              String position
                              ) implements Serializable {
}