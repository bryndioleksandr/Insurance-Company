package org.company.insurance.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.InsurancePolicyCreationDto;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.entity.HealthInsurance;
import org.company.insurance.mapper.InsurancePolicyMapper;
import org.company.insurance.repository.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Transactional
public class InsurancePolicyService {
    private InsurancePolicyRepository insurancePolicyService;
    private InsurancePolicyMapper insurancePolicyMapper;

    public InsurancePolicyDto getInsurancePolicyById(Long id) {
        return insurancePolicyMapper.toDto(insurancePolicyService.findById(id).orElse(null));
    }

    public InsurancePolicyDto createInsurancePolicy(InsurancePolicyCreationDto insurancePolicyDto) {
        return insurancePolicyMapper.toDto(insurancePolicyService.save(insurancePolicyMapper.toEntity(insurancePolicyDto)));
    }

    public InsurancePolicyDto updateInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
        return insurancePolicyMapper.toDto(insurancePolicyService.save(insurancePolicyMapper.toEntity(insurancePolicyDto)));
    }

    public void deleteInsurancePolicyById(Long id) {
        insurancePolicyService.deleteById(id);
    }
}
