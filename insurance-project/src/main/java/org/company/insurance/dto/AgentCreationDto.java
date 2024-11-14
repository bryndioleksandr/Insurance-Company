package org.company.insurance.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.Agent}
 */
public record AgentCreationDto(LocalDateTime createdAt, LocalDateTime updatedAt, String email, String firstName,
                               String surname, String phoneNumber, LocalDate birthDate, LocalDate hireDate,
                               String position) implements Serializable {
}