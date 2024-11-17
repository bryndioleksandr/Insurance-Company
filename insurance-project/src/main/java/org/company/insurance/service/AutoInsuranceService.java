package org.company.insurance.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.AutoInsuranceCreationDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.company.insurance.entity.AutoInsurance;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.repository.AutoInsuranceRepository;
import org.company.insurance.repository.InsurancePolicyRepository;
import org.springframework.stereotype.Service;
import org.company.insurance.mapper.AutoInsuranceMapper;

import java.time.LocalDate;

@AllArgsConstructor
@Service
@Transactional
public class AutoInsuranceService {
    private AutoInsuranceRepository autoInsuranceRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private AutoInsuranceMapper autoInsuranceMapper;

    public AutoInsuranceDto getAutoInsuranceById(Long id) {
        return autoInsuranceMapper.toDto(autoInsuranceRepository.findById(id).orElse(null));
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

    autoInsurance = autoInsuranceRepository.save(autoInsurance);

    InsurancePolicy insurancePolicy = autoInsurance.getInsurancePolicy();
    if (insurancePolicy != null) {
        double updatedPrice = calculatePriceBasedOnAutoInsurance(autoInsurance);

        insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
    }

    return autoInsuranceMapper.toDto(autoInsurance);
}

    private double calculatePriceBasedOnAutoInsurance(AutoInsurance autoInsurance) {
        double basePrice = 500;
        int currentYear = LocalDate.now().getYear();
        int carAge = currentYear - autoInsurance.getYear();

        double ageMultiplier = carAge > 10 ? 1.5 : 1.0;
        double typeMultiplier = switch (autoInsurance.getType()) {
            case CAR -> 1.0;
            case MOTORCYCLE -> 1.2;
            case BUS -> 1.3;
            case TRAILER -> 1.4;
            case TRUCK -> 1.5;
        };

        return basePrice * ageMultiplier * typeMultiplier;
    }

    public AutoInsuranceDto updateAutoInsurance(AutoInsuranceDto autoInsuranceDto) {
        return autoInsuranceMapper.toDto(autoInsuranceRepository.save(autoInsuranceMapper.toEntity(autoInsuranceDto)));
    }

    public void deleteAutoInsuranceById(Long id) {
        autoInsuranceRepository.deleteById(id);
    }
}
