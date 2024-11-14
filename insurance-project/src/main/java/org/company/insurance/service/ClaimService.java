package org.company.insurance.service;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.ClaimCreationDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.mapper.ClaimMapper;
import org.company.insurance.repository.ClaimRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClaimService {
    private ClaimRepository claimRepository;
    private ClaimMapper claimMapper;

    public ClaimDto getClaimById(Long id) {
        return claimMapper.toDto(claimRepository.findById(id).orElse(null));
    }

    public ClaimDto createClaim(ClaimCreationDto claimDto) {
        return claimMapper.toDto(claimRepository.save(claimMapper.toEntity(claimDto)));
    }

    public ClaimDto updateClaim(ClaimDto claimDto) {
        return claimMapper.toDto(claimRepository.save(claimMapper.toEntity(claimDto)));
    }

    public void deleteClaimById(Long id) {
        claimRepository.deleteById(id);
    }
}
