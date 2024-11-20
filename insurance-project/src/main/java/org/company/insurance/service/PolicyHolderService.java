package org.company.insurance.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.PolicyHolderCreationDto;
import org.company.insurance.dto.PolicyHolderDto;
import org.company.insurance.exception.PolicyHolderNotFoundException;
import org.company.insurance.mapper.PolicyHolderMapper;
import org.company.insurance.repository.PolicyHolderRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Transactional
public class PolicyHolderService {
    private PolicyHolderRepository policyHolderRepository;
    private PolicyHolderMapper policyHolderMapper;

    public PolicyHolderDto getPolicyHolderById(Long id) {
        return policyHolderMapper.toDto(policyHolderRepository.findById(id).orElseThrow(() -> new PolicyHolderNotFoundException("Policy holder with id " + id + " not found")));
    }

    public PolicyHolderDto createPolicyHolder(PolicyHolderCreationDto policyHolderDto) {
        return policyHolderMapper.toDto(policyHolderRepository.save(policyHolderMapper.toEntity(policyHolderDto)));
    }

    public PolicyHolderDto updatePolicyHolder(PolicyHolderDto policyHolderDto) {
        return policyHolderMapper.toDto(policyHolderRepository.save(policyHolderMapper.toEntity(policyHolderDto)));
    }

    public void deletePolicyHolderById(Long id) {
        policyHolderRepository.deleteById(id);
    }
}
