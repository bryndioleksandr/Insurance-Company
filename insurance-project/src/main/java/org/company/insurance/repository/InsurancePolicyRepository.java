package org.company.insurance.repository;

import org.company.insurance.entity.Agent;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.InsuranceType;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long>, JpaSpecificationExecutor<InsurancePolicy> {
    @Modifying
    @Query("UPDATE InsurancePolicy ip SET ip.price = :price WHERE ip.id = :id")
    void updatePriceById(@Param("price") Double price, @Param("id") Long id);

    @Modifying
    @Query("UPDATE InsurancePolicy ip SET ip.status = :status WHERE ip.id = :id")
    void updateStatusById(@Param("status") InsuranceStatus status, @Param("id") Long id);

    Page<InsurancePolicy> findByUserId(Long userId, Pageable pageable);

    InsurancePolicy findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    InsurancePolicy findByPolicyNumber(String policyNumber);

    Page<InsurancePolicy> findAllByStatus(InsuranceStatus status, Pageable pageable);

    Page<InsurancePolicy> findByInsuranceType(InsuranceType insuranceType, Pageable pageable);
}