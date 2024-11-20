package org.company.insurance.service;


import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.ClaimCreationDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.entity.AutoInsurance;
import org.company.insurance.entity.Claim;
import org.company.insurance.entity.Claim;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.enums.ClaimType;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.Status;
import org.company.insurance.exception.AutoInsuranceAlreadyExistsException;
import org.company.insurance.exception.ClaimNotFoundException;
import org.company.insurance.mapper.ClaimMapper;
import org.company.insurance.repository.ClaimRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    @Transactional
    public Page<ClaimDto> getAllClaims(Pageable pageable) {
        return claimRepository.findAll(pageable).map(claimMapper::toDto);
    }

    @Transactional
    public Page<ClaimDto> getSortedClaims(String sortBy, String order, Pageable pageable) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Claim> claimPage = claimRepository.findAll(sortedPageable);
        return claimPage.map(claimMapper::toDto);
    }

    @Transactional
    public Page<ClaimDto> getFilteredClaims(Long id, LocalDate submissionDate, double amount, String status, Long insurancePolicy, String incidentDescription, String claimType, Pageable pageable) {
        Specification<Claim> specification = Specification.where(null);

        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + id + "%"));
        }
        if(submissionDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("submissionDate")), "%" + submissionDate + "%"));
        }
        if (amount != 0) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("amount"), "%" + amount + "%"));
        }
        if(status != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + status.toLowerCase() + "%"));
        }
        if (insurancePolicy != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("insurancePolicy"), "%" + insurancePolicy + "%"));
        }
        if(incidentDescription != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("incidentDescription")), "%" + incidentDescription.toLowerCase() + "%"));
        }
        if(claimType != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("claimType")), "%" + claimType.toLowerCase() + "%"));
        }
        Page<Claim> claim = claimRepository.findAll(specification, pageable);
        return claim.map(claimMapper::toDto);
    }
}
