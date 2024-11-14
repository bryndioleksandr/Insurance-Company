package org.company.insurance.repository;

import org.company.insurance.entity.AutoInsurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoInsuranceRepository extends JpaRepository<AutoInsurance, Long> {
}
