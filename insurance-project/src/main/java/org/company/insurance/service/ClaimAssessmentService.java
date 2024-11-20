package org.company.insurance.service;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.transaction.Transactional;
import org.company.insurance.dto.ClaimAssessmentCreationDto;
import org.company.insurance.dto.ClaimAssessmentDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.entity.Agent;
import org.company.insurance.entity.Claim;
import org.company.insurance.entity.ClaimAssessment;
import org.company.insurance.enums.Status;
import org.company.insurance.exception.ClaimAssessmentNotFoundException;
import org.company.insurance.mapper.ClaimAssessmentMapper;
import org.company.insurance.repository.ClaimAssessmentRepository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    @Transactional
    public Page<ClaimAssessmentDto> getAllClaimAssessments(Pageable pageable) {
        return claimAssessmentRepository.findAll(pageable).map(claimAssessmentMapper::toDto);
    }

    @Transactional
    public Page<ClaimAssessmentDto> getSortedClaimAssessments(String sortBy, String order, Pageable pageable) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<ClaimAssessment> claimAssessmentPage = claimAssessmentRepository.findAll(sortedPageable);
        return claimAssessmentPage.map(claimAssessmentMapper::toDto);
    }

    @Transactional
    public Page<ClaimAssessmentDto> getFilteredClaimAssessments(Long id, LocalDate assessmentDate, String notes, double assessmentAmount, Long agent, Pageable pageable) {
        Specification<ClaimAssessment> specification = Specification.where(null);

        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + id + "%"));
        }
        if(assessmentDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("assessmentDate")), "%" + assessmentDate + "%"));
        }
        if (assessmentAmount != 0) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("assessmentAmount"), "%" + assessmentAmount + "%"));
        }
        if(notes != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("notes")), "%" + notes.toLowerCase() + "%"));
        }
        if (agent != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("agent"), "%" + agent + "%"));
        }
        Page<ClaimAssessment> claimAssessment = claimAssessmentRepository.findAll(specification, pageable);
        return claimAssessment.map(claimAssessmentMapper::toDto);
    }
}
