package org.company.insurance.repository;

import org.company.insurance.entity.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {
    @Modifying
    @Query("UPDATE InsurancePolicy ip SET ip.price = :price WHERE ip.id = :id")
    void updatePriceById(@Param("price") double price, @Param("id") Long id);
}