package org.company.insurance.service;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.HealthInsuranceDto;
import org.company.insurance.dto.HealthInsuranceCreationDto;
import org.company.insurance.dto.HealthInsuranceDto;
import org.company.insurance.dto.HealthInsuranceDto;
import org.company.insurance.entity.*;
import org.company.insurance.entity.HealthInsurance;
import org.company.insurance.enums.HealthInsuranceType;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.exception.HealthInsuranceAlreadyExistsException;
import org.company.insurance.exception.HealthInsuranceNotFoundException;
import org.company.insurance.exception.HealthInsuranceAlreadyExistsException;
import org.company.insurance.exception.HealthInsuranceNotFoundException;
import org.company.insurance.mapper.HealthInsuranceMapper;
import org.company.insurance.repository.HealthInsuranceRepository;
import org.company.insurance.repository.InsurancePolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Service
@CacheConfig(cacheResolver = "multiLevelCacheResolver")
public class HealthInsuranceService {
    private HealthInsuranceRepository healthInsuranceRepository;
    private HealthInsuranceMapper healthInsuranceMapper;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private static final Logger logger = LoggerFactory.getLogger(HealthInsuranceService.class);
    

    @Transactional
    @Cacheable
    public HealthInsuranceDto getHealthInsuranceById(Long id) {
        return healthInsuranceMapper.toDto(healthInsuranceRepository.findById(id).orElseThrow(() -> new HealthInsuranceNotFoundException("Health insurance with id " + id + " not found")));
    }

    @Transactional
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

    @Transactional
    public HealthInsuranceDto updateHealthInsuranceByPolicyId(Long policyId, HealthInsuranceDto healthInsuranceDto) {
        logger.info("Updating health insurance for policy ID: {}", policyId);

        HealthInsurance existingHealthInsurance = healthInsuranceRepository.findByInsurancePolicyId(policyId)
                .orElseThrow(() -> {
                    logger.error("Health insurance with policy ID {} not found", policyId);
                    return new HealthInsuranceNotFoundException("Health insurance with policy ID " + policyId + " not found");
                });

        healthInsuranceMapper.partialUpdate(healthInsuranceDto, existingHealthInsurance);
        existingHealthInsurance.setCoverageAmount(calculateHealthInsuranceCoverage(existingHealthInsurance.getInsuranceType()));

        HealthInsurance updatedHealthInsurance = healthInsuranceRepository.save(existingHealthInsurance);

        logger.info("Health insurance updated successfully: {}", updatedHealthInsurance);

        InsurancePolicy insurancePolicy = updatedHealthInsurance.getInsurancePolicy();
        if (insurancePolicy != null) {
            logger.info("Updating price and status for insurance policy ID: {}", insurancePolicy.getId());
            double updatedPrice = calculateHealthInsuranceCost(updatedHealthInsurance, (LocalDate.now().getYear() - insurancePolicy.getUser().getBirthDate().getYear()));
            insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
            insurancePolicyRepository.updateStatusById(InsuranceStatus.valueOf("ACTIVE"), insurancePolicy.getId());
        }
        else {
            logger.warn("No insurance policy found for updated health insurance ID: {}", updatedHealthInsurance.getId());
        }

        return healthInsuranceMapper.toDto(updatedHealthInsurance);
    }

    public HealthInsuranceDto getHealthInsuranceByPolicyId(Long policy){
        return healthInsuranceMapper.toDto(healthInsuranceRepository.findByInsurancePolicyId(policy).orElseThrow(() -> new HealthInsuranceNotFoundException("Health insurance with policy ID " + policy + " not found")));
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

    @Transactional
    public HealthInsuranceDto updateHealthInsurance(HealthInsuranceDto healthInsuranceDto) {
        return healthInsuranceMapper.toDto(healthInsuranceRepository.save(healthInsuranceMapper.toEntity(healthInsuranceDto)));
    }

    @Transactional
    @CacheEvict
    public void deleteHealthInsuranceById(Long id) {
        healthInsuranceRepository.deleteById(id);
    }

    @Transactional
    @Cacheable
    public Page<HealthInsuranceDto> getAllHealth(Pageable pageable) {
        return healthInsuranceRepository.findAll(pageable).map(healthInsuranceMapper::toDto);
    }

    @Transactional
    public Page<HealthInsuranceDto> getSortedHealth(String sortBy, String order, Pageable pageable) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<HealthInsurance> healthPage = healthInsuranceRepository.findAll(sortedPageable);
        return healthPage.map(healthInsuranceMapper::toDto);
    }

    @Transactional
    public Page<HealthInsuranceDto> getFilteredHealth(Long id, String medicalHistory, Long policyId, String insuranceType, Pageable pageable) {
        Specification<HealthInsurance> specification = Specification.where(null);

        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + id + "%"));
        }
        if(medicalHistory != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("medicalHistory")), "%" + medicalHistory.toLowerCase() + "%"));
        }
        if (policyId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("policyId"), "%" + policyId + "%"));
        }
        if(insuranceType != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("insuranceType")), "%" + insuranceType.toLowerCase() + "%"));
        }

        Page<HealthInsurance> health = healthInsuranceRepository.findAll(specification, pageable);
        return health.map(healthInsuranceMapper::toDto);
    }
}
