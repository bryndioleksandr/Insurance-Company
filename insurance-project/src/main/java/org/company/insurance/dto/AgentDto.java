package org.company.insurance.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link org.company.insurance.entity.Agent}
 */
public record AgentDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDate hireDate, String position,
                       UserDto userDto) implements Serializable {
}