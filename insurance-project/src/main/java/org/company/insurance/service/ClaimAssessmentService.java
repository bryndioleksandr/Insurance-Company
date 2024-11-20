package org.company.insurance.service;
import org.company.insurance.dto.ClaimAssessmentCreationDto;
import org.company.insurance.dto.ClaimAssessmentDto;
import org.company.insurance.entity.Claim;
import org.company.insurance.entity.ClaimAssessment;
import org.company.insurance.enums.Status;
import org.company.insurance.exception.ClaimAssessmentNotFoundException;
import org.company.insurance.mapper.ClaimAssessmentMapper;
import org.company.insurance.repository.ClaimAssessmentRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class ClaimAssessmentService {
    private ClaimAssessmentRepository claimAssessmentRepository;
    private ClaimAssessmentMapper claimAssessmentMapper;

    public ClaimAssessmentDto getClaimAssessmentById(Long id) {
        return claimAssessmentMapper.toDto(claimAssessmentRepository.findById(id).orElseThrow(() -> new ClaimAssessmentNotFoundException("Claim assessment with id " + id + " not found")));
    }

    public ClaimAssessmentDto createClaimAssessment(ClaimAssessmentCreationDto claimAssessmentDto) {
        ClaimAssessment claimAssessment = claimAssessmentMapper.toEntity(claimAssessmentDto);

        claimAssessment.setAssessmentDate(LocalDate.now());
        claimAssessment = claimAssessmentRepository.save(claimAssessment);

        claimAssessment.setAssessmentDate(LocalDate.now());
        return claimAssessmentMapper.toDto(claimAssessment);
        //return claimAssessmentMapper.toDto(claimAssessmentRepository.save(claimAssessmentMapper.toEntity(claimAssessmentDto)));
    }

    public ClaimAssessmentDto updateClaimAssessment(ClaimAssessmentDto claimAssessmentDto) {
        return claimAssessmentMapper.toDto(claimAssessmentRepository.save(claimAssessmentMapper.toEntity(claimAssessmentDto)));
    }

    public void deleteClaimAssessmentById(Long id) {
        claimAssessmentRepository.deleteById(id);
    }
}
