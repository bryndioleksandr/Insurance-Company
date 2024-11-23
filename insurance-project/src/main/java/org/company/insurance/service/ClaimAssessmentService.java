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
import org.company.insurance.entity.User;
import org.company.insurance.enums.Status;
import org.company.insurance.exception.ClaimAssessmentNotFoundException;
import org.company.insurance.mapper.ClaimAssessmentMapper;
import org.company.insurance.repository.AgentRepository;
import org.company.insurance.repository.ClaimAssessmentRepository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
@CacheConfig(cacheResolver = "multiLevelCacheResolver")
public class ClaimAssessmentService {
    private static final Logger logger = LoggerFactory.getLogger(ClaimAssessmentService.class);

    private ClaimAssessmentRepository claimAssessmentRepository;
    private ClaimAssessmentMapper claimAssessmentMapper;
    private UserService userService;
    private AgentRepository agentRepository;

    @Transactional
    @Cacheable
    public ClaimAssessmentDto getClaimAssessmentById(Long id) {
        logger.info("Fetching claim assessment with id: {}", id);
        ClaimAssessment claimAssessment = claimAssessmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Claim assessment with id {} not found", id);
                    return new ClaimAssessmentNotFoundException("Claim assessment with id " + id + " not found");
                });
        return claimAssessmentMapper.toDto(claimAssessment);
    }

    public ClaimAssessmentDto createClaimAssessment(ClaimAssessmentCreationDto claimAssessmentDto) {
        logger.info("Creating claim assessment with details: {}", claimAssessmentDto);
        ClaimAssessment claimAssessment = claimAssessmentMapper.toEntity(claimAssessmentDto);

        claimAssessment.setAgent(agentRepository.findByUserId(userService.getCurrentUser()));
        claimAssessment.setAssessmentDate(LocalDate.now());
        claimAssessment = claimAssessmentRepository.save(claimAssessment);

        logger.info("Claim assessment created with id: {}", claimAssessment.getId());
        return claimAssessmentMapper.toDto(claimAssessment);
    }

    public ClaimAssessmentDto updateClaimAssessment(ClaimAssessmentDto claimAssessmentDto) {
        logger.info("Updating claim assessment with id: {}", claimAssessmentDto.id());
        ClaimAssessment claimAssessment = claimAssessmentMapper.toEntity(claimAssessmentDto);
        claimAssessment = claimAssessmentRepository.save(claimAssessment);

        logger.info("Claim assessment updated with id: {}", claimAssessment.getId());
        return claimAssessmentMapper.toDto(claimAssessment);
    }

    public void deleteClaimAssessmentById(Long id) {
        logger.info("Deleting claim assessment with id: {}", id);
        claimAssessmentRepository.deleteById(id);
        logger.info("Claim assessment with id: {} deleted", id);
    }

    @Transactional
    @Cacheable
    public Page<ClaimAssessmentDto> getAllClaimAssessments(Pageable pageable) {
        logger.info("Fetching all claim assessments with pagination: {}", pageable);
        Page<ClaimAssessment> claimAssessmentPage = claimAssessmentRepository.findAll(pageable);
        logger.info("Fetched {} claim assessments", claimAssessmentPage.getTotalElements());
        return claimAssessmentPage.map(claimAssessmentMapper::toDto);
    }

    @Transactional
    public Page<ClaimAssessmentDto> getSortedClaimAssessments(String sortBy, String order, Pageable pageable) {
        logger.info("Fetching sorted claim assessments by: {} in order: {}", sortBy, order);
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<ClaimAssessment> claimAssessmentPage = claimAssessmentRepository.findAll(sortedPageable);
        logger.info("Fetched {} sorted claim assessments", claimAssessmentPage.getTotalElements());
        return claimAssessmentPage.map(claimAssessmentMapper::toDto);
    }

    @Transactional
    public Page<ClaimAssessmentDto> getFilteredClaimAssessments(Long id, LocalDate assessmentDate, String notes, double assessmentAmount, Long agent, Pageable pageable) {
        logger.info("Fetching filtered claim assessments with filters: id = {}, assessmentDate = {}, notes = {}, assessmentAmount = {}, agent = {}", id, assessmentDate, notes, assessmentAmount, agent);

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

        Page<ClaimAssessment> claimAssessmentPage = claimAssessmentRepository.findAll(specification, pageable);
        logger.info("Fetched {} filtered claim assessments", claimAssessmentPage.getTotalElements());
        return claimAssessmentPage.map(claimAssessmentMapper::toDto);
    }
}
