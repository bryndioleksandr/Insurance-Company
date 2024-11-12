package org.company.insurance.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.Person}
 */
public record PersonDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String email, String firstName,
                        String surname, String phoneNumber, Date birthDate) implements Serializable {
}