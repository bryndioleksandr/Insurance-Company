package org.company.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.PolicyHolder}
 */
public record PolicyHolderCreationDto(Long userId,
                                      @NotBlank @Pattern(regexp = "^\\d{9}$") String passportNumber,
                                      @NotBlank(message = "Address can not be blank. Format: country, city, street and house number")
                                      String address) implements Serializable {
}