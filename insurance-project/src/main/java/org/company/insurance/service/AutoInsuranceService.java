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

    @Transactional
    @Cacheable
    public AutoInsuranceDto getAutoInsuranceById(Long id) {
        return autoInsuranceMapper.toDto(autoInsuranceRepository.findById(id).orElseThrow(() -> new AutoInsuranceNotFoundException("Auto insurance with id " + id + " not found")));
    }

//    public AutoInsuranceDto createAutoInsurance(AutoInsuranceCreationDto autoInsuranceDto) {
//        return autoInsuranceMapper.toDto(autoInsuranceRepository.save(autoInsuranceMapper.toEntity(autoInsuranceDto)));
//        AutoInsurance autoInsurance = autoInsuranceMapper.toEntity(autoInsuranceDto);
//        InsurancePolicy insurancePolicy = autoInsurance.getInsurancePolicy();
//
//
//    }
public AutoInsuranceDto createAutoInsurance(AutoInsuranceCreationDto autoInsuranceDto) {
    AutoInsurance autoInsurance = autoInsuranceMapper.toEntity(autoInsuranceDto);

    if(autoInsuranceRepository.existsByInsurancePolicyId(autoInsuranceDto.insurancePolicyId())){
        throw new AutoInsuranceAlreadyExistsException("Auto insurance for policy with id " + autoInsuranceDto.insurancePolicyId() + " already exists");
    }
    autoInsurance.setCoverageAmount(calculateCoverageAmount(autoInsurance.getInsuranceType()));
    autoInsurance = autoInsuranceRepository.save(autoInsurance);

    InsurancePolicy insurancePolicy = autoInsurance.getInsurancePolicy();
    if (insurancePolicy != null) {
        double updatedPrice = calculatePriceBasedOnAutoInsurance(autoInsurance);

        insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
        insurancePolicyRepository.updateStatusById(InsuranceStatus.valueOf("ACTIVE"), insurancePolicy.getId());
    }

    autoInsurance.setCoverageAmount(calculateCoverageAmount(autoInsurance.getInsuranceType()));
    return autoInsuranceMapper.toDto(autoInsurance);
}

    private double calculatePriceBasedOnAutoInsurance(AutoInsurance autoInsurance) {
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
        System.out.println("Days diff is " + daysDifference);
        System.out.println(basePrice);
        System.out.println(ageMultiplier);
        System.out.println(typeMultiplier);
        System.out.println(capacityMultiplier);

        return basePrice * ageMultiplier * typeMultiplier * capacityMultiplier * longevityMultiplier;
    }

    private double calculateCoverageAmount(AutoInsuranceType type) {
        return switch (type) {
            case OSAGO -> 50000.0;
            case KASKO -> 100000.0;
            case CIVIL_LIABILITY -> 30000.0;
            case THEFT_VANDALISM -> 70000.0;
            case ALL_INCLUSIVE -> 200000.0;
        };
    }

    public AutoInsuranceDto updateAutoInsurance(AutoInsuranceDto autoInsuranceDto) {
        return autoInsuranceMapper.toDto(autoInsuranceRepository.save(autoInsuranceMapper.toEntity(autoInsuranceDto)));
    }

    @Transactional
    @CacheEvict
    public void deleteAutoInsuranceById(Long id) {
        autoInsuranceRepository.deleteById(id);
    }

    @Transactional
    @Cacheable
    public Page<AutoInsuranceDto> getAllAutoInsurances(Pageable pageable) {
        return autoInsuranceRepository.findAll(pageable).map(autoInsuranceMapper::toDto);
    }

    @Transactional
    public Page<AutoInsuranceDto> getSortedAutoInsurances(String sortBy, String order, Pageable pageable) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<AutoInsurance> autoInsurancePage = autoInsuranceRepository.findAll(sortedPageable);
        return autoInsurancePage.map(autoInsuranceMapper::toDto);
    }

    @Transactional
    public Page<AutoInsuranceDto> getFilteredAutoInsurances(Long id, double engineCapacity, String brand, String model, int year, String plate, String type, String insuranceType, Long insurancePolicy, Pageable pageable) {
        Specification<AutoInsurance> specification = Specification.where(null);

        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + id + "%"));
        }
        if(brand != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%"));
        }
        if(model != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("model")), "%" + model.toLowerCase() + "%"));
        }
        if (engineCapacity != 0) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("engineCapacity"), "%" + engineCapacity + "%"));
        }
        if (year != 0) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("year"), "%" + year + "%"));
        }
        if(plate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("plate")), "%" + plate.toLowerCase() + "%"));
        }
        if(type != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("type")), "%" + type.toLowerCase() + "%"));
        }
        if(insuranceType != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("insuranceType")), "%" + insuranceType.toLowerCase() + "%"));
        }
        if (insurancePolicy != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("insurancePolicy"), "%" + insurancePolicy + "%"));
        }
        Page<AutoInsurance> autoInsurance = autoInsuranceRepository.findAll(specification, pageable);
        return autoInsurance.map(autoInsuranceMapper::toDto);
    }
}
