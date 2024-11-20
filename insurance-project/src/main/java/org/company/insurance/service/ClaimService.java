package org.company.insurance.service;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.ClaimCreationDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.entity.AutoInsurance;
import org.company.insurance.entity.Claim;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.Status;
import org.company.insurance.exception.AutoInsuranceAlreadyExistsException;
import org.company.insurance.exception.ClaimNotFoundException;
import org.company.insurance.mapper.ClaimMapper;
import org.company.insurance.repository.ClaimRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class ClaimService {
    private ClaimRepository claimRepository;
    private ClaimMapper claimMapper;

    public ClaimDto getClaimById(Long id) {

        return claimMapper.toDto(claimRepository.findById(id).orElseThrow(() -> new ClaimNotFoundException("Claim with id " + id + " not found")));
    }

    public ClaimDto createClaim(ClaimCreationDto claimDto) {
        Claim claim = claimMapper.toEntity(claimDto);

        claim.setStatus(Status.PENDING);
        claim.setSubmissionDate(LocalDate.now());

        claim = claimRepository.save(claim);

        claim.setSubmissionDate(LocalDate.now());
        claim.setStatus(Status.PENDING);
        return claimMapper.toDto(claim);
       // return claimMapper.toDto(claimRepository.save(claimMapper.toEntity(claimDto)));
    }

    public ClaimDto updateClaim(ClaimDto claimDto) {
        return claimMapper.toDto(claimRepository.save(claimMapper.toEntity(claimDto)));
    }

    public void deleteClaimById(Long id) {
        claimRepository.deleteById(id);
    }
}
