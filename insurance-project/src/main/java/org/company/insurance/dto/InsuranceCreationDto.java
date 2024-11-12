package org.company.insurance.dto;

import java.io.Serializable;

/**
 * DTO for {@link org.company.insurance.entity.Insurance}
 */
public record InsuranceCreationDto(double coverageAmount, int insuranceLongevity) implements Serializable {
}