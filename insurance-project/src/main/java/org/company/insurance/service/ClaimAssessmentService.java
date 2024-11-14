package org.company.insurance.service;
import org.company.insurance.dto.ClaimAssessmentCreationDto;
import org.company.insurance.dto.ClaimAssessmentDto;
import org.company.insurance.mapper.ClaimAssessmentMapper;
import org.company.insurance.repository.ClaimAssessmentRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClaimAssessmentService {
    private ClaimAssessmentRepository claimAssessmentRepository;
    private ClaimAssessmentMapper claimAssessmentMapper;

    public ClaimAssessmentDto getClaimAssessmentById(Long id) {
        return claimAssessmentMapper.toDto(claimAssessmentRepository.findById(id).orElse(null));
    }

    public ClaimAssessmentDto createClaimAssessment(ClaimAssessmentCreationDto claimAssessmentDto) {
        return claimAssessmentMapper.toDto(claimAssessmentRepository.save(claimAssessmentMapper.toEntity(claimAssessmentDto)));
    }

    public ClaimAssessmentDto updateClaimAssessment(ClaimAssessmentDto claimAssessmentDto) {
        return claimAssessmentMapper.toDto(claimAssessmentRepository.save(claimAssessmentMapper.toEntity(claimAssessmentDto)));
    }

    public void deleteClaimAssessmentById(Long id) {
        claimAssessmentRepository.deleteById(id);
    }
}
