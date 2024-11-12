package org.company.insurance.dto;

import org.company.insurance.enums.ClaimType;
import org.company.insurance.enums.Status;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link org.company.insurance.entity.Claim}
 */
public record ClaimDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Date submissionDate, Double amount, Status status, String incidentDescription, ClaimType claimType) implements Serializable {
  }