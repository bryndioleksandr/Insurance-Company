package org.company.insurance.repository;

import org.company.insurance.entity.Agent;
import org.company.insurance.entity.AutoInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutoInsuranceRepository extends JpaRepository<AutoInsurance, Long>, JpaSpecificationExecutor<AutoInsurance> {
    @Query("SELECT COUNT(a) > 0 FROM AutoInsurance a WHERE a.insurancePolicy.id = ?1")
    boolean existsByInsurancePolicyId(Long insurancePolicyId);

    Optional <AutoInsurance> findByInsurancePolicyId(Long insurancePolicyId);

    @Query("SELECT a, i FROM AutoInsurance a JOIN a.insurancePolicy i WHERE a.insurancePolicy.id = ?1")
    Object[] findByIdWithPolicyDetails(Long id);
}
