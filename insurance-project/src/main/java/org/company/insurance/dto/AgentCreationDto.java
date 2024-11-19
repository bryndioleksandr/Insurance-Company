package org.company.insurance.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.Agent}
 */
public record AgentCreationDto(LocalDateTime createdAt, LocalDateTime updatedAt,
//                               @Email
//                               @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email should be valid")
//                               @NotBlank @Size(max = 255)
//                               String email,
//                               @NotBlank @Size(max = 255) String firstName,
//                               @NotBlank @Size(max = 255) String surname,
//                               @NotBlank @Size(max = 255)
//                               @Pattern(regexp = "^\\+\\d{1,4}\\d{7,15}$", message = "Phone number must be in the format +<country code><phone number>")
//                               String phoneNumber,
//                               LocalDate birthDate,
                               Long userId,
                               @Past
                               LocalDate hireDate,
                               @NotBlank @Size(max = 255)
                               String position) implements Serializable {
}