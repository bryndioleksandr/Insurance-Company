package org.company.insurance.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.InsurancePolicyCreationDto;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.entity.*;
import org.company.insurance.exception.HealthInsuranceNotFoundException;
import org.company.insurance.mapper.InsurancePolicyMapper;
import org.company.insurance.repository.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Transactional
public class InsurancePolicyService {
    private InsurancePolicyRepository insurancePolicyService;
    private InsurancePolicyMapper insurancePolicyMapper;
    private UserRepository userRepository;
    private PolicyHolderRepository policyHolderRepository;

//    private InsuranceStatus determineInsuranceStatus(InsurancePolicy policy) {
//        LocalDate endDate = policy.getEndDate();
//        if (endDate != null && LocalDate.now().isAfter(endDate)) {
//            return InsuranceStatus.EXPIRED;
//        }
//        return InsuranceStatus.ACTIVE;
//    }
//
//    @Transactional
//    @PostConstruct
//    public void checkAndUpdateInsuranceStatusOnStartup() {
//        List<InsurancePolicy> policies = insurancePolicyService.findAll();
//
//        for (InsurancePolicy policy : policies) {
//            InsuranceStatus status = determineInsuranceStatus(policy);
//            if (status != policy.getStatus()) {
//                policy.setStatus(status);
//                insurancePolicyService.updateStatusById(status, policy.getId());
//            }
//        }
//    }

    public InsurancePolicyDto getInsurancePolicyById(Long id) {
        return insurancePolicyMapper.toDto(insurancePolicyService.findById(id).orElseThrow(() -> new HealthInsuranceNotFoundException("Health insurance policy with id " + id + " not found")));
    }

    private String generateRandomPolicyNumber() {
        return String.format("%08d", (int) (Math.random() * 100000000));
    }

    public InsurancePolicyDto createInsurancePolicy(InsurancePolicyCreationDto insurancePolicyDto) {
        InsurancePolicy insurancePolicy = insurancePolicyMapper.toEntity(insurancePolicyDto);

        insurancePolicy.setPolicyNumber(generateRandomPolicyNumber());

        User user = userRepository.findById(insurancePolicyDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PolicyHolder policyHolder = policyHolderRepository.findByUserId(user)
                .orElseGet(() -> {
                    PolicyHolder newPolicyHolder = new PolicyHolder();
                    newPolicyHolder.setUserId(user);
                    newPolicyHolder.setPassportNumber(insurancePolicyDto.passportNumber());
                    newPolicyHolder.setAddress(insurancePolicyDto.address());
                    return policyHolderRepository.save(newPolicyHolder);
                });

        insurancePolicy.setPolicyHolder(policyHolder);

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
