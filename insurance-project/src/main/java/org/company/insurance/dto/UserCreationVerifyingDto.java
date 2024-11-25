package org.company.insurance.dto;

import org.company.insurance.enums.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.company.insurance.entity.User}
 */
public record UserCreationVerifyingDto(String username,
                                       String password,
                                       String email,
                                       String emailVerificationCode,
                                       LocalDateTime emailVerificationExpiry,
                                       boolean emailVerified,
                                       Role role) implements Serializable {
}