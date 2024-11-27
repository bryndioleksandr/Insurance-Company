package org.company.insurance.repository;

import org.company.insurance.entity.Agent;
import org.company.insurance.entity.AutoInsurance;
import org.company.insurance.entity.HealthInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance, Long>, JpaSpecificationExecutor<HealthInsurance> {
    @Query("SELECT COUNT(a) > 0 FROM AutoInsurance a WHERE a.insurancePolicy.id = ?1")
    boolean existsByInsurancePolicyId(Long insurancePolicyId);

    Optional<HealthInsurance> findByInsurancePolicyId(Long insurancePolicyId);

    @Query("SELECT a, i FROM HealthInsurance a JOIN a.insurancePolicy i WHERE a.insurancePolicy.id= ?1")
    Object[] findByIdWithPolicyDetails(Long id);
}