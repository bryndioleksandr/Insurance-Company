package org.company.insurance.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.company.insurance.entity.Agent;
import org.company.insurance.entity.Claim;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.ClaimAssessment}
 */


public record ClaimAssessmentCreationDto(Long claimId,
                                         @PastOrPresent LocalDate assessmentDate,
                                         @NotBlank @Size(max = 512) String notes,
                                         @NotBlank Double assessmentAmount,
                                         Long agentId) implements Serializable {
}