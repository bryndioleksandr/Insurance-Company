package org.company.insurance.service;


import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.PropertyInsuranceCreationDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.entity.*;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.PropertyInsuranceType;

import org.company.insurance.exception.PropertyInsuranceAlreadyExistsException;
import org.company.insurance.exception.PropertyInsuranceNotFoundException;
import org.company.insurance.mapper.PropertyInsuranceMapper;
import org.company.insurance.repository.InsurancePolicyRepository;
import org.company.insurance.repository.PropertyInsuranceRepository;
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

@Service
@AllArgsConstructor
@Transactional
@CacheConfig(cacheResolver = "multiLevelCacheResolver")
public class PropertyInsuranceService {
    private final PropertyInsuranceRepository propertyInsuranceRepository;
    private final PropertyInsuranceMapper propertyInsuranceMapper;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private static final Logger logger = LoggerFactory.getLogger(PropertyInsuranceService.class);
    

    private double calculatePropertyInsurancePrice(PropertyInsurance propertyInsurance) {
        Long currentInsurancePolicyId = propertyInsurance.getInsurancePolicy().getId();
        LocalDate startDate = insurancePolicyRepository.findById(currentInsurancePolicyId).get().getStartDate();
        LocalDate endDate = insurancePolicyRepository.findById(currentInsurancePolicyId).get().getEndDate();

        long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        double longevityMultiplier = daysDifference / 365.0;

        double houseSize = propertyInsurance.getHouseSize();
        double basePricePerCubicMeter = 25.0;
        return basePricePerCubicMeter * houseSize * longevityMultiplier;
    }

    @Transactional
    public PropertyInsuranceDto getPropertyInsuranceByPolicyId(Long policyId){
        return propertyInsuranceMapper.toDto(propertyInsuranceRepository.findByInsurancePolicyId(policyId).orElseThrow(() -> new PropertyInsuranceNotFoundException("Property insurance with policy ID " + policyId + " not found")));
    }

    private double calculateCoverageAmount(double houseSize) {
        PropertyInsuranceType coverageAmount = PropertyInsuranceType.getCoverageAmount(houseSize);
        return coverageAmount.getAmount();
    }

    @Transactional
    @Cacheable
    public PropertyInsuranceDto getPropertyInsuranceById(Long id) {
        return propertyInsuranceMapper.toDto(propertyInsuranceRepository.findById(id).orElseThrow(() -> new PropertyInsuranceNotFoundException("Property insurance with id " + id + " not found")));
    }

    @Transactional
    public PropertyInsuranceDto createPropertyInsurance(PropertyInsuranceCreationDto propertyInsuranceDto) {
        PropertyInsurance propertyInsurance = propertyInsuranceMapper.toEntity(propertyInsuranceDto);

        if(propertyInsuranceRepository.existsByInsurancePolicyId(propertyInsuranceDto.insurancePolicyId())){
            throw new PropertyInsuranceAlreadyExistsException("Property insurance for policy with id " + propertyInsuranceDto.insurancePolicyId() + " already exists");
        }
        propertyInsurance.setCoverageAmount(calculateCoverageAmount(propertyInsurance.getHouseSize()));
        propertyInsurance = propertyInsuranceRepository.save(propertyInsurance);

        InsurancePolicy insurancePolicy = propertyInsurance.getInsurancePolicy();
        if (insurancePolicy != null) {
            double updatedPrice = calculatePropertyInsurancePrice(propertyInsurance);

            insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
            insurancePolicyRepository.updateStatusById(InsuranceStatus.valueOf("ACTIVE"), insurancePolicy.getId());
        }

        propertyInsurance.setCoverageAmount(calculateCoverageAmount(propertyInsurance.getHouseSize()));
        return propertyInsuranceMapper.toDto(propertyInsurance);

        // return propertyInsuranceMapper.toDto(propertyInsuranceRepository.save(propertyInsuranceMapper.toEntity(propertyInsuranceDto)));
    }

    @Transactional
    public PropertyInsuranceDto updatePropertyInsuranceByPolicyId(Long policyId, PropertyInsuranceDto propertyInsuranceDto) {
        logger.info("Updating property insurance for policy ID: {}", policyId);

        PropertyInsurance existingPropertyInsurance = propertyInsuranceRepository.findByInsurancePolicyId(policyId)
                .orElseThrow(() -> {
                    logger.error("Property insurance with policy ID {} not found", policyId);
                    return new PropertyInsuranceNotFoundException("Property insurance with policy ID " + policyId + " not found");
                });

        propertyInsuranceMapper.partialUpdate(propertyInsuranceDto, existingPropertyInsurance);
        existingPropertyInsurance.setCoverageAmount(calculateCoverageAmount(existingPropertyInsurance.getHouseSize()));

        PropertyInsurance updatedPropertyInsurance = propertyInsuranceRepository.save(existingPropertyInsurance);

        logger.info("Property insurance updated successfully: {}", updatedPropertyInsurance);

        InsurancePolicy insurancePolicy = updatedPropertyInsurance.getInsurancePolicy();
        if (insurancePolicy != null) {
            logger.info("Updating price and status for insurance policy ID: {}", insurancePolicy.getId());
            double updatedPrice = calculatePropertyInsurancePrice(updatedPropertyInsurance);
            insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
            insurancePolicyRepository.updateStatusById(InsuranceStatus.valueOf("ACTIVE"), insurancePolicy.getId());
        } else {
            logger.warn("No insurance policy found for updated property insurance ID: {}", updatedPropertyInsurance.getId());
        }
        return propertyInsuranceMapper.toDto(updatedPropertyInsurance);

    }

    @Transactional
    public PropertyInsuranceDto updatePropertyInsurance(PropertyInsuranceDto propertyInsuranceDto) {
        return propertyInsuranceMapper.toDto(propertyInsuranceRepository.save(propertyInsuranceMapper.toEntity(propertyInsuranceDto)));
    }

    @Transactional
    @CacheEvict
    public void deletePropertyInsuranceById(Long id) {
        propertyInsuranceRepository.deleteById(id);
    }

    @Transactional
    @Cacheable
    public Page<PropertyInsuranceDto> getAllProperty(Pageable pageable) {
        return propertyInsuranceRepository.findAll(pageable).map(propertyInsuranceMapper::toDto);
    }

    @Transactional
    public Page<PropertyInsuranceDto> getSortedProperty(String sortBy, String order, Pageable pageable) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<PropertyInsurance> travelsPage = propertyInsuranceRepository.findAll(sortedPageable);
        return travelsPage.map(propertyInsuranceMapper::toDto);
    }

    @Transactional
    public Page<PropertyInsuranceDto> getFilteredProperty(Long id, String propertyAddress, double houseSize, String insuranceType, Pageable pageable) {
        Specification<PropertyInsurance> specification = Specification.where(null);

        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + id + "%"));
        }
        if(propertyAddress != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("propertyAddress")), "%" + propertyAddress.toLowerCase() + "%"));
        }
        if (houseSize != 0) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("houseSize"), "%" + houseSize + "%"));
        }
        if(insuranceType != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("insuranceType")), "%" + insuranceType.toLowerCase() + "%"));
        }

        Page<PropertyInsurance> property = propertyInsuranceRepository.findAll(specification, pageable);
        return property.map(propertyInsuranceMapper::toDto);
    }
}
