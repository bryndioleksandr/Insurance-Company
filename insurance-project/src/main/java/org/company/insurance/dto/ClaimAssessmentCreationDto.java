package org.company.insurance.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.ClaimAssessment}
 */
public record ClaimAssessmentCreationDto(Date assesmentDate, String notes,
                                         Double assessmentAmount) implements Serializable {
}