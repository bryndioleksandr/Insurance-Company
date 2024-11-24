package org.company.insurance.repository;

import org.company.insurance.entity.Agent;
import org.company.insurance.entity.AutoInsurance;
import org.company.insurance.entity.PropertyInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PropertyInsuranceRepository extends JpaRepository<PropertyInsurance, Long>, JpaSpecificationExecutor<PropertyInsurance> {
    @Query("SELECT COUNT(a) > 0 FROM AutoInsurance a WHERE a.insurancePolicy.id = ?1")
    boolean existsByInsurancePolicyId(Long insurancePolicyId);

    Optional<PropertyInsurance> findByInsurancePolicyId(Long insurancePolicyId);

}