package org.company.insurance.service;

import lombok.AllArgsConstructor;
import org.company.insurance.dto.HealthInsuranceCreationDto;
import org.company.insurance.dto.HealthInsuranceDto;
import org.company.insurance.mapper.HealthInsuranceMapper;
import org.company.insurance.repository.HealthInsuranceRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class HealthInsuranceService {
    private HealthInsuranceRepository healthInsuranceRepository;
    private HealthInsuranceMapper healthInsuranceMapper;

    public HealthInsuranceDto getHealthInsuranceById(Long id) {
        return healthInsuranceMapper.toDto(healthInsuranceRepository.findById(id).orElse(null));
    }

//    public HealthInsuranceDto createHealthInsurance(HealthInsuranceCreationDto healthInsuranceDto) {
//        return healthInsuranceMapper.toDto(healthInsuranceRepository.save(healthInsuranceMapper.toEntity(healthInsuranceDto)));
//    }

    public HealthInsuranceDto updateHealthInsurance(HealthInsuranceDto healthInsuranceDto) {
        return healthInsuranceMapper.toDto(healthInsuranceRepository.save(healthInsuranceMapper.toEntity(healthInsuranceDto)));
    }

    public void deleteHealthInsuranceById(Long id) {
        healthInsuranceRepository.deleteById(id);
    }
}
