package org.company.insurance.repository;

import org.company.insurance.entity.TravelInsurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelInsuranceRepository extends JpaRepository<TravelInsurance, Long> {
}