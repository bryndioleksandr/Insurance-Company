package org.company.insurance.service;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.AutoInsuranceCreationDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.company.insurance.entity.AutoInsurance;
import org.company.insurance.entity.AutoInsurance;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.enums.AutoInsuranceType;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.VehicleType;
import org.company.insurance.exception.AutoInsuranceAlreadyExistsException;
import org.company.insurance.exception.AutoInsuranceNotFoundException;
import org.company.insurance.repository.AutoInsuranceRepository;
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
import org.company.insurance.mapper.AutoInsuranceMapper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@AllArgsConstructor
@Service
@Transactional
@CacheConfig(cacheResolver = "multiLevelCacheResolver")
public class AutoInsuranceService {
    private AutoInsuranceRepository autoInsuranceRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private AutoInsuranceMapper autoInsuranceMapper;

    private static final Logger logger = LoggerFactory.getLogger(AutoInsuranceService.class);

    @Transactional
    @Cacheable
    public AutoInsuranceDto getAutoInsuranceById(Long id) {
        logger.info("Fetching auto insurance by ID: {}", id);
        AutoInsurance autoInsurance = autoInsuranceRepository.findById(id).orElseThrow(() -> {
            logger.error("Auto insurance with id {} not found", id);
            return new AutoInsuranceNotFoundException("Auto insurance with id " + id + " not found");
        });
        logger.info("Found auto insurance: {}", autoInsurance);
        return autoInsuranceMapper.toDto(autoInsurance);
    }

    public AutoInsuranceDto createAutoInsurance(AutoInsuranceCreationDto autoInsuranceDto) {
        logger.info("Creating auto insurance for policy ID: {}", autoInsuranceDto.insurancePolicyId());

        AutoInsurance autoInsurance = autoInsuranceMapper.toEntity(autoInsuranceDto);

        if (autoInsuranceRepository.existsByInsurancePolicyId(autoInsuranceDto.insurancePolicyId())) {
            logger.error("Auto insurance for policy with id {} already exists", autoInsuranceDto.insurancePolicyId());
            throw new AutoInsuranceAlreadyExistsException("Auto insurance for policy with id " + autoInsuranceDto.insurancePolicyId() + " already exists");
        }

        autoInsurance.setCoverageAmount(calculateCoverageAmount(autoInsurance.getInsuranceType()));
        autoInsurance = autoInsuranceRepository.save(autoInsurance);
        logger.info("Auto insurance created: {}", autoInsurance);

        InsurancePolicy insurancePolicy = autoInsurance.getInsurancePolicy();
        if (insurancePolicy != null) {
            logger.info("Updating price and status for insurance policy ID: {}", insurancePolicy.getId());
            double updatedPrice = calculatePriceBasedOnAutoInsurance(autoInsurance);
            insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
            insurancePolicyRepository.updateStatusById(InsuranceStatus.valueOf("ACTIVE"), insurancePolicy.getId());
        }


        return autoInsuranceMapper.toDto(autoInsurance);
    }

    @Transactional
    public AutoInsuranceDto updateAutoInsuranceByPolicyId(Long policyId, AutoInsuranceDto autoInsuranceDto) {
        logger.info("Updating auto insurance for policy ID: {}", policyId);

        AutoInsurance existingAutoInsurance = autoInsuranceRepository.findByInsurancePolicyId(policyId)
                .orElseThrow(() -> {
                    logger.error("Auto insurance with policy ID {} not found", policyId);
                    return new AutoInsuranceNotFoundException("Auto insurance with policy ID " + policyId + " not found");
                });
        existingAutoInsurance.setCoverageAmount(calculateCoverageAmount(existingAutoInsurance.getInsuranceType()));

        autoInsuranceMapper.partialUpdate(autoInsuranceDto, existingAutoInsurance);
        existingAutoInsurance.setCoverageAmount(calculateCoverageAmount(existingAutoInsurance.getInsuranceType()));

        AutoInsurance updatedAutoInsurance = autoInsuranceRepository.save(existingAutoInsurance);

        logger.info("Auto insurance updated successfully: {}", updatedAutoInsurance);

        InsurancePolicy insurancePolicy = updatedAutoInsurance.getInsurancePolicy();
        if (insurancePolicy != null) {
            logger.info("Updating price and status for insurance policy ID: {}", insurancePolicy.getId());
            double updatedPrice = calculatePriceBasedOnAutoInsurance(updatedAutoInsurance);
            insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
            insurancePolicyRepository.updateStatusById(InsuranceStatus.valueOf("ACTIVE"), insurancePolicy.getId());
        }
        else {
            logger.warn("No insurance policy found for updated auto insurance ID: {}", updatedAutoInsurance.getId());
        }

        return autoInsuranceMapper.toDto(updatedAutoInsurance);
    }

    private double calculatePriceBasedOnAutoInsurance(AutoInsurance autoInsurance) {
        logger.info("Calculating price based on auto insurance: {}", autoInsurance);

        Long currentInsurancePolicyId = autoInsurance.getInsurancePolicy().getId();
        LocalDate startDate = insurancePolicyRepository.findById(currentInsurancePolicyId).get().getStartDate();
        LocalDate endDate = insurancePolicyRepository.findById(currentInsurancePolicyId).get().getEndDate();
        long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        double basePrice = 500;
        int currentYear = LocalDate.now().getYear();
        int carAge = currentYear - autoInsurance.getYear();
        double capacity = autoInsurance.getEngineCapacity();

        double ageMultiplier = carAge > 10 ? 1.5 : 1.2;
        double capacityMultiplier;
        if (capacity > 3000) {
            capacityMultiplier = 1.8;
        } else if (capacity > 2500) {
            capacityMultiplier = 1.6;
        } else if (capacity > 2000) {
            capacityMultiplier = 1.4;
        } else if (capacity > 1500) {
            capacityMultiplier = 1.2;
        } else if (capacity > 1000) {
            capacityMultiplier = 1.0;
        } else {
            capacityMultiplier = 0.8;
        }
        double typeMultiplier = switch (autoInsurance.getType()) {
            case CAR -> 1.0;
            case MOTORCYCLE -> 0.8;
            case BUS -> 1.4;
            case TRAILER -> 0.9;
            case TRUCK -> 1.5;
        };

        double longevityMultiplier = daysDifference / 365.0;
        logger.debug("Days diff is {}, Base price is {}, Age multiplier is {}, Type multiplier is {}, Capacity multiplier is {}",
                daysDifference, basePrice, ageMultiplier, typeMultiplier, capacityMultiplier);

        double price = basePrice * ageMultiplier * typeMultiplier * capacityMultiplier * longevityMultiplier;
        logger.debug("Calculated price: {}", price);
        return price;
    }

    private double calculateCoverageAmount(AutoInsuranceType type) {
        logger.info("Calculating coverage amount for insurance type: {}", type);
        double coverageAmount = switch (type) {
            case OSAGO -> 50000.0;
            case KASKO -> 100000.0;
            case CIVIL_LIABILITY -> 30000.0;
            case THEFT_VANDALISM -> 70000.0;
            case ALL_INCLUSIVE -> 200000.0;
        };
        logger.debug("Calculated coverage amount: {}", coverageAmount);
        return coverageAmount;
    }

    public AutoInsuranceDto updateAutoInsurance(AutoInsuranceDto autoInsuranceDto) {
        logger.info("Updating auto insurance: {}", autoInsuranceDto);
        AutoInsurance autoInsurance = autoInsuranceRepository.save(autoInsuranceMapper.toEntity(autoInsuranceDto));
        logger.info("Auto insurance updated: {}", autoInsurance);
        return autoInsuranceMapper.toDto(autoInsurance);
    }

    @Transactional
    @CacheEvict
    public void deleteAutoInsuranceById(Long id) {
        logger.info("Deleting auto insurance with ID: {}", id);
        autoInsuranceRepository.deleteById(id);
        logger.info("Auto insurance with ID {} deleted successfully", id);
    }

    @Transactional
    @Cacheable
    public Page<AutoInsuranceDto> getAllAutoInsurances(Pageable pageable) {
        logger.info("Fetching all auto insurances with pagination: {}", pageable);
        Page<AutoInsurance> autoInsurancePage = autoInsuranceRepository.findAll(pageable);
        logger.info("Fetched {} auto insurances", autoInsurancePage.getTotalElements());
        return autoInsurancePage.map(autoInsuranceMapper::toDto);
    }

    @Transactional
    public Page<AutoInsuranceDto> getSortedAutoInsurances(String sortBy, String order, Pageable pageable) {
        logger.info("Fetching sorted auto insurances by {} in {} order", sortBy, order);
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<AutoInsurance> autoInsurancePage = autoInsuranceRepository.findAll(sortedPageable);
        logger.info("Fetched {} sorted auto insurances", autoInsurancePage.getTotalElements());
        return autoInsurancePage.map(autoInsuranceMapper::toDto);
    }

    @Transactional
    public Page<AutoInsuranceDto> getFilteredAutoInsurances(Long id, double engineCapacity, String brand, String model, int year, String plate, String type, String insuranceType, Long insurancePolicy, Pageable pageable) {
        logger.info("Fetching filtered auto insurances with parameters: id={}, engineCapacity={}, brand={}, model={}, year={}, plate={}, type={}, insuranceType={}, insurancePolicy={}",
                id, engineCapacity, brand, model, year, plate, type, insuranceType, insurancePolicy);

        Specification<AutoInsurance> specification = Specification.where(null);

        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("id"), "%" + id + "%"));
        }
        if (brand != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%"));
        }
        if (model != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("model")), "%" + model.toLowerCase() + "%"));
        }
        if (engineCapacity != 0) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("engineCapacity"), "%" + engineCapacity + "%"));
        }
        if (year != 0) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("year"), "%" + year + "%"));
        }
        if (plate != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("plate")), "%" + plate.toLowerCase() + "%"));
        }
        if (type != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("type")), "%" + type.toLowerCase() + "%"));
        }
        if (insuranceType != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("insuranceType")), "%" + insuranceType.toLowerCase() + "%"));
        }
        if (insurancePolicy != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("insurancePolicy").get("id"), insurancePolicy));
        }

        logger.info("Filtering by specification: {}", specification);
        Page<AutoInsurance> autoInsurancePage = autoInsuranceRepository.findAll(specification, pageable);
        logger.info("Fetched {} filtered auto insurances", autoInsurancePage.getTotalElements());
        return autoInsurancePage.map(autoInsuranceMapper::toDto);
    }
}
