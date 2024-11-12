package org.company.insurance.repository;

import org.company.insurance.entity.PropertyInsurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyInsuranceRepository extends JpaRepository<PropertyInsurance, Long> {
}