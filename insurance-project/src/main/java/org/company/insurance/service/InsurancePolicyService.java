package org.company.insurance.service;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.*;
import org.company.insurance.entity.*;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.InsuranceType;
import org.company.insurance.exception.HealthInsuranceNotFoundException;
import org.company.insurance.exception.InsurancePolicyNotFoundException;
import org.company.insurance.mapper.InsurancePolicyMapper;
import org.company.insurance.repository.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.company.insurance.enums.InsuranceType.VEHICLE;

@AllArgsConstructor
@Service
@Transactional
@CacheConfig(cacheResolver = "multiLevelCacheResolver")
public class InsurancePolicyService {
    private final AutoInsuranceRepository autoInsuranceRepository;
    private final TravelInsuranceRepository travelInsuranceRepository;
    private final PropertyInsuranceRepository propertyInsuranceRepository;
    private final HealthInsuranceRepository healthInsuranceRepository;
    private InsurancePolicyRepository insurancePolicyRepository;
    private InsurancePolicyMapper insurancePolicyMapper;
    private UserRepository userRepository;
    private PolicyHolderRepository policyHolderRepository;
    private UserService userService;
    private AutoInsuranceService autoInsuranceService;
    private TravelInsuranceService travelInsuranceService;
    private PropertyInsuranceService propertyInsuranceService;
    private HealthInsuranceService healthInsuranceService;

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
//        List<InsurancePolicy> policies = insurancePolicyRepository.findAll();
//
//        for (InsurancePolicy policy : policies) {
//            InsuranceStatus status = determineInsuranceStatus(policy);
//            if (status != policy.getStatus()) {
//                policy.setStatus(status);
//                insurancePolicyRepository.updateStatusById(status, policy.getId());
//            }
//        }
//    }

    @Transactional
    @Cacheable
    public InsurancePolicyDto getInsurancePolicyById(Long id) {
        return insurancePolicyMapper.toDto(insurancePolicyRepository.findById(id).orElseThrow(() -> new HealthInsuranceNotFoundException("Health insurance policy with id " + id + " not found")));
    }



    private String generateRandomPolicyNumber() {
        return String.format("%08d", (int) (Math.random() * 100000000));
    }

    @Transactional
    public InsurancePolicyDto createInsurancePolicy(InsurancePolicyCreationDto insurancePolicyDto) {
        InsurancePolicy insurancePolicy = insurancePolicyMapper.toEntity(insurancePolicyDto);
        User user = userService.getCurrentUser();

        insurancePolicy.setPolicyNumber(generateRandomPolicyNumber());
        insurancePolicy.setUser(user);
        insurancePolicy.setStatus(InsuranceStatus.INACTIVE);

//        User user = userRepository.findById(insurancePolicyDto.userId())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String insuranceType = insurancePolicyDto.insuranceType().toString();

        PolicyHolder policyHolder = policyHolderRepository.findByUserId(user)
                .orElseGet(() -> {
                    PolicyHolder newPolicyHolder = new PolicyHolder();
                    newPolicyHolder.setUserId(user);
                    newPolicyHolder.setPassportNumber(insurancePolicyDto.passportNumber());
                    newPolicyHolder.setAddress(insurancePolicyDto.address());
                    return policyHolderRepository.save(newPolicyHolder);
                });

        insurancePolicy.setPolicyHolder(policyHolder);

        insurancePolicy = insurancePolicyRepository.save(insurancePolicy);

        LocalDate now = LocalDate.now();
        switch (insuranceType) {
            case "VEHICLE" -> {
                AutoInsurance autoInsurance = new AutoInsurance();
                autoInsurance.setInsurancePolicy(insurancePolicy);
                autoInsurance.setInsuranceLongevity(initLongevity(insurancePolicy));
                autoInsuranceRepository.save(autoInsurance);
            }
            case "TRAVEL" -> {
                TravelInsurance travelInsurance = new TravelInsurance();
                travelInsurance.setInsurancePolicy(insurancePolicy);
                travelInsurance.setInsuranceLongevity(initLongevity(insurancePolicy));
                travelInsuranceRepository.save(travelInsurance);
            }
            case "PROPERTY" -> {
                PropertyInsurance propertyInsurance = new PropertyInsurance();
                propertyInsurance.setInsurancePolicy(insurancePolicy);
                propertyInsurance.setInsuranceLongevity(initLongevity(insurancePolicy));
                propertyInsuranceRepository.save(propertyInsurance);
            }
            case "HEALTH" -> {
                HealthInsurance healthInsurance = new HealthInsurance();
                healthInsurance.setInsurancePolicy(insurancePolicy);
                healthInsurance.setInsuranceLongevity(initLongevity(insurancePolicy));
                healthInsuranceRepository.save(healthInsurance);
            }
        }

        return insurancePolicyMapper.toDto(insurancePolicy);
    }

    public int initLongevity(InsurancePolicy insurancePolicy) {
        LocalDate now = LocalDate.now();
        if (now.isBefore(insurancePolicy.getStartDate())) {
            return (int) ChronoUnit.DAYS.between(insurancePolicy.getStartDate(), insurancePolicy.getEndDate());
        } else if (now.isAfter(insurancePolicy.getStartDate()) && now.isBefore(insurancePolicy.getEndDate())) {
            return (int) ChronoUnit.DAYS.between(now, insurancePolicy.getEndDate());
        } else {
            return 0;
        }
    }

    @Transactional
    public InsurancePolicyDto updateInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
        return insurancePolicyMapper.toDto(insurancePolicyRepository.save(insurancePolicyMapper.toEntity(insurancePolicyDto)));
    }

    @Transactional
    @CacheEvict
    public void deleteInsurancePolicyById(Long id) {
        insurancePolicyRepository.deleteById(id);
    }

    @Transactional
    @Cacheable
    public Page<InsurancePolicyDto> getAllInsurances(Pageable pageable) {
        return insurancePolicyRepository.findAll(pageable).map(insurancePolicyMapper::toDto);
    }

    @Transactional
    public Page<InsurancePolicyDto> getSortedInsurances(String sortBy, String order, Pageable pageable) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<InsurancePolicy> insurancePolicies = insurancePolicyRepository.findAll(sortedPageable);
        return insurancePolicies.map(insurancePolicyMapper::toDto);
    }

    @Transactional
    public Page<InsurancePolicyDto> getFilteredInsurances(Long id, String policyNumber, Long userId, LocalDate startDate, LocalDate endDate, double price, String status, String insuranceType, Long policyHolder, Pageable pageable) {
        Specification<InsurancePolicy> specification = Specification.where(null);

        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + id + "%"));
        }
        if(policyNumber != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("policyNumber")), "%" + policyNumber + "%"));
        }
        if (userId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("userId"), "%" + userId + "%"));
        }
        if (startDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("startDate"), "%" + startDate + "%"));
        }
        if (endDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("endDate"), "%" + endDate + "%"));
        }
        if (price != 0) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("price"), "%" + price + "%"));
        }
        if(status != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + status.toLowerCase() + "%"));
        }
        if(insuranceType != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("insuranceType")), "%" + insuranceType.toLowerCase() + "%"));
        }
        if (policyHolder != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("policyHolder"), "%" + policyHolder + "%"));
        }

        Page<InsurancePolicy> insurances = insurancePolicyRepository.findAll(specification, pageable);
        return insurances.map(insurancePolicyMapper::toDto);
    }

    @Transactional
    @Cacheable
    public Page<InsurancePolicyDto> getAllInsurancesForCurrentUser(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        return insurancePolicyRepository.findByUserId(currentUser.getId(), pageable)
                .map(insurancePolicyMapper::toDto);
    }

    @Transactional
    //@Cacheable(key = "{#policyId, #root.target.userService.getCurrentUser().getId()}")
    @Cacheable
    public AutoInsuranceDto getAutoInsuranceByPolicyId(Long policyId){
        User currentUser = userService.getCurrentUser();
        if(!insurancePolicyRepository.existsByIdAndUserId(policyId, currentUser.getId())){
            throw new InsurancePolicyNotFoundException("Insurance policy with ID " + policyId + " not found or doesnt belong to " + currentUser.getUsername());
        }
        return autoInsuranceService.getAutoInsuranceByPolicyId(policyId);
    }

    @Transactional
    @Cacheable
    public TravelInsuranceDto getTravelInsuranceByPolicyId(Long policyId){
        User currentUser = userService.getCurrentUser();
        if(!insurancePolicyRepository.existsByIdAndUserId(policyId, currentUser.getId())){
            throw new InsurancePolicyNotFoundException("Insurance policy with ID " + policyId + " not found or doesnt belong to " + currentUser.getUsername());
        }
        return travelInsuranceService.getTravelInsuranceByPolicyId(policyId);
    }

    @Transactional
    @Cacheable
    public PropertyInsuranceDto getPropertyInsuranceByPolicyId(Long policyId){
        User currentUser = userService.getCurrentUser();
        if(!insurancePolicyRepository.existsByIdAndUserId(policyId, currentUser.getId())){
            throw new InsurancePolicyNotFoundException("Insurance policy with ID " + policyId + " not found or doesnt belong to " + currentUser.getUsername());
        }
        return propertyInsuranceService.getPropertyInsuranceByPolicyId(policyId);
    }

    @Transactional
    @Cacheable
    public HealthInsuranceDto getHealthInsuranceByPolicyId(Long policyId){
        User currentUser = userService.getCurrentUser();
        if(!insurancePolicyRepository.existsByIdAndUserId(policyId, currentUser.getId())){
            throw new InsurancePolicyNotFoundException("Insurance policy with ID " + policyId + " not found or doesnt belong to " + currentUser.getUsername());
        }
        return healthInsuranceService.getHealthInsuranceByPolicyId(policyId);
    }

    @Transactional
    public Page<InsurancePolicyDto> getSortedInsurancesForCurrentUser(String sortBy, String order, Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return insurancePolicyRepository.findByUserId(currentUser.getId(), sortedPageable)
                .map(insurancePolicyMapper::toDto);
    }

    @Transactional
    public Page<InsurancePolicyDto> getFilteredInsurancesForCurrentUser(Long id, String policyNumber, Long userId, LocalDate startDate, LocalDate endDate, double price, String status, String insuranceType, Long policyHolder, Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        Specification<InsurancePolicy> specification = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), currentUser.getId()));

        if (policyNumber != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("policyNumber")), "%" + policyNumber.toLowerCase() + "%"));
        }
        if (startDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
        }
        if (endDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
        }

        return insurancePolicyRepository.findAll(specification, pageable).map(insurancePolicyMapper::toDto);
    }

}
