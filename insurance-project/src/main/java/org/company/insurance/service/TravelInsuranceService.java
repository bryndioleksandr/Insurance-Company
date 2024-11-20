package org.company.insurance.service;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.TravelInsuranceCreationDto;
import org.company.insurance.dto.TravelInsuranceDto;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.entity.PropertyInsurance;
import org.company.insurance.entity.TravelInsurance;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.exception.PropertyInsuranceAlreadyExistsException;
import org.company.insurance.exception.TravelInsuranceAlreadyExistsException;
import org.company.insurance.exception.TravelInsuranceNotFoundException;
import org.company.insurance.mapper.TravelInsuranceMapper;
import org.company.insurance.repository.InsurancePolicyRepository;
import org.company.insurance.repository.TravelInsuranceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class TravelInsuranceService {
    private final TravelInsuranceRepository travelInsuranceRepository;
    private final TravelInsuranceMapper travelInsuranceMapper;

    private final InsurancePolicyRepository insurancePolicyRepository;

    private double calculateTravelInsurancePrice(TravelInsurance insurance) {
        Long currentInsurancePolicyId = insurance.getInsurancePolicy().getId();
        LocalDate startDate = insurancePolicyRepository.findById(currentInsurancePolicyId).get().getStartDate();
        LocalDate endDate = insurancePolicyRepository.findById(currentInsurancePolicyId).get().getEndDate();

        long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        double longevityMultiplier = daysDifference / 365.0;

        double basePrice = 100;

        double typeMultiplier = switch (insurance.getTravelType()) {
            case BUSINESS -> 1.5;
            case LEISURE -> 1.2;
            case STUDENT -> 1.1;
        };

        double areaMultiplier = switch (insurance.getCoverageArea()) {
            case EUROPE_UK -> 1.0;
            case WORLDWIDE_EXCLUDING_USA_CANADA -> 1.3;
            case WORLDWIDE -> 1.5;
        };

        return basePrice * typeMultiplier * areaMultiplier * longevityMultiplier;
    }

    private double calculateCoverageAmount(TravelInsurance insurance) {
        return switch (insurance.getCoverageArea()) {
            case EUROPE_UK -> 100000.0;
            case WORLDWIDE_EXCLUDING_USA_CANADA -> 200000.0;
            case WORLDWIDE -> 300000.0;
        };
    }

    public TravelInsuranceDto getTravelInsuranceById(Long id) {
        return travelInsuranceMapper.toDto(travelInsuranceRepository.findById(id).orElseThrow(() -> new TravelInsuranceNotFoundException("Travel insurance with id " + id + " not found")));
    }

    public TravelInsuranceDto createTravelInsurance(TravelInsuranceCreationDto travelInsuranceDto) {
        TravelInsurance travelInsurance = travelInsuranceMapper.toEntity(travelInsuranceDto);

        if(travelInsuranceRepository.existsByInsurancePolicyId(travelInsuranceDto.insurancePolicyId())){
            throw new TravelInsuranceAlreadyExistsException("Travel insurance for policy with id " + travelInsuranceDto.insurancePolicyId() + " already exists");
        }
        travelInsurance.setCoverageAmount(calculateCoverageAmount(travelInsurance));
        travelInsurance = travelInsuranceRepository.save(travelInsurance);

        InsurancePolicy insurancePolicy = travelInsurance.getInsurancePolicy();
        if (insurancePolicy != null) {
            double updatedPrice = calculateTravelInsurancePrice(travelInsurance);

            insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
            insurancePolicyRepository.updateStatusById(InsuranceStatus.valueOf("ACTIVE"), insurancePolicy.getId());
        }

        travelInsurance.setCoverageAmount(calculateCoverageAmount(travelInsurance));
        return travelInsuranceMapper.toDto(travelInsurance);

        // return travelInsuranceMapper.toDto(travelInsuranceRepository.save(travelInsuranceMapper.toEntity(travelInsuranceDto)));
    }

    public TravelInsuranceDto updateTravelInsurance(TravelInsuranceDto travelInsuranceDto) {
        return travelInsuranceMapper.toDto(travelInsuranceRepository.save(travelInsuranceMapper.toEntity(travelInsuranceDto)));
    }

    public void deleteTravelInsuranceById(Long id) {
        travelInsuranceRepository.deleteById(id);
    }
}
