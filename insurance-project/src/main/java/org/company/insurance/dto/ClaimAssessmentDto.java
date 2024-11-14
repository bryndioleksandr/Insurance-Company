package org.company.insurance.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.ClaimAssessment}
 */
public record ClaimAssessmentDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Long claimId,
                                 LocalDate assesmentDate, String notes, Double assessmentAmount,
                                 Long agentId) implements Serializable {
}