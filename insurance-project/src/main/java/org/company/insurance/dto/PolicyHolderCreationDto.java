package org.company.insurance.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.PolicyHolder}
 */
public record PolicyHolderCreationDto(String email, String firstName, String surname, String phoneNumber,
                                      Date birthDate, String passportNumber, String address) implements Serializable {
}