package org.company.insurance.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.company.insurance.enums.ClaimType;
import org.company.insurance.enums.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.Claim}
 */
public record ClaimCreationDto(LocalDate submissionDate,
                               @Min(20) @Max(500000) Double amount,
                               @NotBlank @Size(max = 512)
                               String incidentDescription,
                               @NotBlank ClaimType claimType,
                               Status status,
                               Long insurancePolicyId) implements Serializable {
}