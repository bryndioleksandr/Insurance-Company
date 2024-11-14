package org.company.insurance.service;

import lombok.AllArgsConstructor;
import org.company.insurance.dto.InsurancePolicyCreationDto;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.mapper.InsurancePolicyMapper;
import org.company.insurance.repository.InsurancePolicyRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
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
