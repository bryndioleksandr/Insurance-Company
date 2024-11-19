package org.company.insurance.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.InsurancePolicyCreationDto;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.entity.AutoInsurance;
import org.company.insurance.entity.HealthInsurance;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.mapper.InsurancePolicyMapper;
import org.company.insurance.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class InsurancePolicyService {
    private InsurancePolicyRepository insurancePolicyService;
    private InsurancePolicyMapper insurancePolicyMapper;

    private InsuranceStatus determineInsuranceStatus(InsurancePolicy policy) {
        LocalDate endDate = policy.getEndDate();
        if (endDate != null && LocalDate.now().isAfter(endDate)) {
            return InsuranceStatus.EXPIRED;
        }
        return InsuranceStatus.ACTIVE;
    }

    @PostConstruct
    public void checkAndUpdateInsuranceStatusOnStartup() {
        List<InsurancePolicy> policies = insurancePolicyService.findAll();

        for (InsurancePolicy policy : policies) {
            InsuranceStatus status = determineInsuranceStatus(policy);
            if (status != policy.getStatus()) {
                policy.setStatus(status);
                insurancePolicyService.updateStatusById(status, policy.getId());
            }
        }
    }

    public InsurancePolicyDto getInsurancePolicyById(Long id) {
        return insurancePolicyMapper.toDto(insurancePolicyService.findById(id).orElse(null));
    }

    private String generateRandomPolicyNumber() {
        return String.format("%08d", (int) (Math.random() * 100000000));
    }

    public InsurancePolicyDto createInsurancePolicy(InsurancePolicyCreationDto insurancePolicyDto) {
        InsurancePolicy insurancePolicy = insurancePolicyMapper.toEntity(insurancePolicyDto);

        insurancePolicy.setPolicyNumber(generateRandomPolicyNumber());

        insurancePolicy = insurancePolicyService.save(insurancePolicy);

        return insurancePolicyMapper.toDto(insurancePolicy);
    }

    public InsurancePolicyDto updateInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
        return insurancePolicyMapper.toDto(insurancePolicyService.save(insurancePolicyMapper.toEntity(insurancePolicyDto)));
    }

    public void deleteInsurancePolicyById(Long id) {
        insurancePolicyService.deleteById(id);
    }
}
