package org.company.insurance.repository;

import org.company.insurance.entity.HealthInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance, Long> {
    @Query("SELECT COUNT(a) > 0 FROM AutoInsurance a WHERE a.insurancePolicy.id = ?1")
    boolean existsByInsurancePolicyId(Long insurancePolicyId);
}