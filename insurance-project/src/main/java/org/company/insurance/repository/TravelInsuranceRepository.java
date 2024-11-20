package org.company.insurance.repository;

import org.company.insurance.entity.Agent;
import org.company.insurance.entity.TravelInsurance;
import org.company.insurance.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TravelInsuranceRepository extends JpaRepository<TravelInsurance, Long>, JpaSpecificationExecutor<TravelInsurance> {
    @Query("SELECT COUNT(a) > 0 FROM AutoInsurance a WHERE a.insurancePolicy.id = ?1")
    boolean existsByInsurancePolicyId(Long insurancePolicyId);

}