package org.company.insurance.dto;

import org.company.insurance.entity.Agent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link Agent}
 */
public record AgentDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String email, String firstName,
                       String surname, String phoneNumber, Date birthDate, Date hireDate,
                       String position) implements Serializable {
}