package org.company.insurance.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.PolicyHolder}
 */
public record PolicyHolderDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String email, String firstName,
                              String surname, String phoneNumber, Date birthDate, String passportNumber,
                              String address) implements Serializable {
}