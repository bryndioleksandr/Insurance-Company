package org.company.insurance.dto;

import jakarta.validation.constraints.*;
import org.company.insurance.enums.Role;

import java.io.Serializable;
import java.time.LocalDate;
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
//                              @Past
//                              LocalDate hireDate,
//                              @Size(max = 255)
//                              String position
                              ) implements Serializable {
}