package org.company.insurance.service;

import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.PolicyHolderCreationDto;
import org.company.insurance.dto.PolicyHolderDto;
import org.company.insurance.dto.PolicyHolderDto;
import org.company.insurance.entity.PolicyHolder;
import org.company.insurance.entity.User;
//import org.company.insurance.enums.PolicyHolderType;
import org.company.insurance.exception.PolicyHolderNotFoundException;
import org.company.insurance.mapper.PolicyHolderMapper;
import org.company.insurance.repository.PolicyHolderRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Transactional
@CacheConfig(cacheResolver = "multiLevelCacheResolver")
public class PolicyHolderService {
    private PolicyHolderRepository policyHolderRepository;
    private PolicyHolderMapper policyHolderMapper;

    @Transactional
    @Cacheable
    public PolicyHolderDto getPolicyHolderById(Long id) {
        return policyHolderMapper.toDto(policyHolderRepository.findById(id).orElseThrow(() -> new PolicyHolderNotFoundException("Policy holder with id " + id + " not found")));
    }

    @Transactional
    public PolicyHolderDto createPolicyHolder(PolicyHolderCreationDto policyHolderDto) {
        return policyHolderMapper.toDto(policyHolderRepository.save(policyHolderMapper.toEntity(policyHolderDto)));
    }

    @Transactional
    public PolicyHolderDto updatePolicyHolder(PolicyHolderDto policyHolderDto) {
        return policyHolderMapper.toDto(policyHolderRepository.save(policyHolderMapper.toEntity(policyHolderDto)));
    }

    @Transactional
    public void deletePolicyHolderById(Long id) {
        policyHolderRepository.deleteById(id);
    }

    @Transactional
    @Cacheable
    public Page<PolicyHolderDto> getAllHolders(Pageable pageable) {
        return policyHolderRepository.findAll(pageable).map(policyHolderMapper::toDto);
    }

    @Transactional
    public Page<PolicyHolderDto> getSortedHolders(String sortBy, String order, Pageable pageable) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<PolicyHolder> holdersPage = policyHolderRepository.findAll(sortedPageable);
        return holdersPage.map(policyHolderMapper::toDto);
    }

    @Transactional
    public Page<PolicyHolderDto> getFilteredHolders(Long id, String passportNumber, String address, Pageable pageable) {
        Specification<PolicyHolder> specification = Specification.where(null);

        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + id + "%"));
        }
        if(passportNumber != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("passportNumber")), "%" + passportNumber + "%"));
        }
        if (address != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("address"), "%" + address.toLowerCase() + "%"));
        }

        Page<PolicyHolder> holders = policyHolderRepository.findAll(specification, pageable);
        return holders.map(policyHolderMapper::toDto);
    }
}
