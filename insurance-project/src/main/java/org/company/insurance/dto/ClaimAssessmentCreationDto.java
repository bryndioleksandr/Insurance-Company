package org.company.insurance.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import org.company.insurance.entity.Agent;
import org.company.insurance.entity.Claim;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.ClaimAssessment}
 */


public record ClaimAssessmentCreationDto(Long claimId, LocalDate assessmentDate, String notes,
                                         Double assessmentAmount, Long agentId) implements Serializable {
}