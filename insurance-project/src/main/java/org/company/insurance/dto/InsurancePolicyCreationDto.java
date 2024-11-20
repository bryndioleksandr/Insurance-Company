package org.company.insurance.dto;

import jakarta.validation.constraints.*;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.InsuranceType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.InsurancePolicy}
 */
public record InsurancePolicyCreationDto(
                                          String policyNumber,

                                          @NotNull(message = "User ID cannot be null")
                                          Long userId,

                                          @NotNull(message = "Start date cannot be null")
                                          @PastOrPresent(message = "Start date must be in the past or present")
                                          LocalDate startDate,

                                          @NotNull(message = "End date cannot be null")
                                          @FutureOrPresent(message = "End date must be in the future or present")
                                          LocalDate endDate,

                                          @NotNull(message = "Price cannot be null")
                                          @Positive(message = "Price must be a positive number")
                                          double price,

                                          @NotNull(message = "Insurance status cannot be null")
                                          InsuranceStatus status,

                                          @NotNull(message = "Insurance type cannot be null")
                                          InsuranceType insuranceType,

                                         // @NotNull(message = "Policy holder ID cannot be null")
                                          Long policyHolderId,
                                          @Pattern(regexp = "^\\d{9}$") String passportNumber,
                                          String address) implements Serializable {

}