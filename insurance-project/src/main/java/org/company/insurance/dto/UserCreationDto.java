package org.company.insurance.dto;

import org.company.insurance.enums.Role;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.User}
 */
public record UserCreationDto(String email, String firstName, String surname, String phoneNumber, Date birthDate,
                              String username, String password, Role role) implements Serializable {
}