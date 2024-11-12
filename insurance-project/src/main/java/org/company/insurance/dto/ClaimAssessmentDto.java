package org.company.insurance.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.ClaimAssessment}
 */
public record ClaimAssessmentDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Date assesmentDate, String notes, Double assessmentAmount) implements Serializable {
  }