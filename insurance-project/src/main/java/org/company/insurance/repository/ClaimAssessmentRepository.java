package org.company.insurance.repository;

import org.company.insurance.entity.Agent;
import org.company.insurance.entity.Claim;
import org.company.insurance.entity.ClaimAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClaimAssessmentRepository extends JpaRepository<ClaimAssessment, Long>, JpaSpecificationExecutor<ClaimAssessment> {
}