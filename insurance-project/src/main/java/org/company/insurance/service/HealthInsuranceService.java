package org.company.insurance.service;

import lombok.AllArgsConstructor;
import org.company.insurance.dto.HealthInsuranceCreationDto;
import org.company.insurance.dto.HealthInsuranceDto;
import org.company.insurance.entity.AutoInsurance;
import org.company.insurance.entity.HealthInsurance;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.enums.HealthInsuranceType;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.exception.AutoInsuranceAlreadyExistsException;
import org.company.insurance.exception.HealthInsuranceAlreadyExistsException;
import org.company.insurance.mapper.HealthInsuranceMapper;
import org.company.insurance.repository.HealthInsuranceRepository;
import org.company.insurance.repository.InsurancePolicyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Service
public class HealthInsuranceService {
    private HealthInsuranceRepository healthInsuranceRepository;
    private HealthInsuranceMapper healthInsuranceMapper;
    private final InsurancePolicyRepository insurancePolicyRepository;

    public HealthInsuranceDto getHealthInsuranceById(Long id) {
        return healthInsuranceMapper.toDto(healthInsuranceRepository.findById(id).orElse(null));
    }

    public HealthInsuranceDto createHealthInsurance(HealthInsuranceCreationDto healthInsuranceDto) {
        HealthInsurance healthInsurance = healthInsuranceMapper.toEntity(healthInsuranceDto);

        if(healthInsuranceRepository.existsByInsurancePolicyId(healthInsuranceDto.insurancePolicyId())){
            throw new HealthInsuranceAlreadyExistsException("Health insurance for policy with id " + healthInsuranceDto.insurancePolicyId() + " already exists");
        }
        healthInsurance.setCoverageAmount(calculateHealthInsuranceCoverage(healthInsurance.getInsuranceType()));
        healthInsurance = healthInsuranceRepository.save(healthInsurance);

        InsurancePolicy insurancePolicy = healthInsurance.getInsurancePolicy();
        if (insurancePolicy != null) {
            double updatedPrice = calculateHealthInsuranceCost(healthInsurance, (LocalDate.now().getYear() - insurancePolicy.getUser().getBirthDate().getYear()));

            insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
            insurancePolicyRepository.updateStatusById(InsuranceStatus.valueOf("ACTIVE"), insurancePolicy.getId());
        }

        healthInsurance.setCoverageAmount(calculateHealthInsuranceCoverage(healthInsurance.getInsuranceType()));
        return healthInsuranceMapper.toDto(healthInsurance);
        // return healthInsuranceMapper.toDto(healthInsuranceRepository.save(healthInsuranceMapper.toEntity(healthInsuranceDto)));
    }

    private double calculateHealthInsuranceCost(HealthInsurance healthInsurance, int age) {
        Long currentInsurancePolicyId = healthInsurance.getInsurancePolicy().getId();
        LocalDate startDate = insurancePolicyRepository.findById(currentInsurancePolicyId).get().getStartDate();
        LocalDate endDate = insurancePolicyRepository.findById(currentInsurancePolicyId).get().getEndDate();
        long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        double longevityMultiplier = daysDifference / 365.0;

        HealthInsuranceType serviceType = healthInsurance.getInsuranceType();
        double basePrice = 300;
        double ageMultiplier = (age > 60) ? 1.5 : 1.0;

        double serviceMultiplier = switch (serviceType) {
            case STANDARD -> 1.0;
            case MEDICAL -> 1.8;
            case DENTAL -> 1.2;
            case VISION -> 1.1;
            case MENTAL -> 1.3;
        };

        return basePrice * serviceMultiplier * ageMultiplier * longevityMultiplier;
    }

    private double calculateHealthInsuranceCoverage(HealthInsuranceType insuranceType) {
        return switch (insuranceType) {
            case DENTAL -> 20000.0;
            case VISION -> 10000.0;
            case MEDICAL -> 50000.0;
            case MENTAL -> 15000.0;
            case STANDARD -> 30000.0;
        };
    }

    public HealthInsuranceDto updateHealthInsurance(HealthInsuranceDto healthInsuranceDto) {
        return healthInsuranceMapper.toDto(healthInsuranceRepository.save(healthInsuranceMapper.toEntity(healthInsuranceDto)));
    }

    public void deleteHealthInsuranceById(Long id) {
        healthInsuranceRepository.deleteById(id);
    }
}
