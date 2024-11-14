package org.company.insurance.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.ClaimAssessment}
 */
public record ClaimAssessmentCreationDto(LocalDate assesmentDate, String notes,
                                         Double assessmentAmount) implements Serializable {
}