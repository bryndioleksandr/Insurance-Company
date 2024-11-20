package org.company.insurance.service;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.PropertyInsuranceCreationDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.entity.HealthInsurance;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.entity.PropertyInsurance;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.PropertyInsuranceType;
import org.company.insurance.exception.HealthInsuranceAlreadyExistsException;
import org.company.insurance.exception.PropertyInsuranceAlreadyExistsException;
import org.company.insurance.exception.PropertyInsuranceNotFoundException;
import org.company.insurance.mapper.PropertyInsuranceMapper;
import org.company.insurance.repository.InsurancePolicyRepository;
import org.company.insurance.repository.PropertyInsuranceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class PropertyInsuranceService {
    private final PropertyInsuranceRepository propertyInsuranceRepository;
    private final PropertyInsuranceMapper propertyInsuranceMapper;
    private final InsurancePolicyRepository insurancePolicyRepository;

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

    private double calculateCoverageAmount(double houseSize) {
        PropertyInsuranceType coverageAmount = PropertyInsuranceType.getCoverageAmount(houseSize);
        return coverageAmount.getAmount();
    }

    public PropertyInsuranceDto getPropertyInsuranceById(Long id) {
        return propertyInsuranceMapper.toDto(propertyInsuranceRepository.findById(id).orElseThrow(() -> new PropertyInsuranceNotFoundException("Property insurance with id " + id + " not found")));
    }

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

    public PropertyInsuranceDto updatePropertyInsurance(PropertyInsuranceDto propertyInsuranceDto) {
        return propertyInsuranceMapper.toDto(propertyInsuranceRepository.save(propertyInsuranceMapper.toEntity(propertyInsuranceDto)));
    }

    public void deletePropertyInsuranceById(Long id) {
        propertyInsuranceRepository.deleteById(id);
    }
}
