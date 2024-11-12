package org.company.insurance.dto;

import org.company.insurance.enums.InsuranceType;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.InsurancePolicy}
 */
public record InsurancePolicyCreationDto(String policyNumber, Date startDate, Date endDate, double price,
                                         InsuranceType insuranceType) implements Serializable {
}