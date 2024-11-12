package org.company.insurance.repository;

import org.company.insurance.entity.ClaimAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimAssessmentRepository extends JpaRepository<ClaimAssessment, Long> {
}