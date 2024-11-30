package org.company.insurance.service;


import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.TravelInsuranceCreationDto;
import org.company.insurance.dto.TravelInsuranceDto;
import org.company.insurance.dto.UserDto;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.entity.TravelInsurance;
import org.company.insurance.entity.User;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.exception.TravelInsuranceAlreadyExistsException;
import org.company.insurance.exception.TravelInsuranceNotFoundException;
import org.company.insurance.mapper.TravelInsuranceMapper;
import org.company.insurance.repository.InsurancePolicyRepository;
import org.company.insurance.repository.TravelInsuranceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
@Transactional
@CacheConfig(cacheResolver = "multiLevelCacheResolver")
public class TravelInsuranceService {
    private final TravelInsuranceRepository travelInsuranceRepository;
    private final TravelInsuranceMapper travelInsuranceMapper;

    private final InsurancePolicyRepository insurancePolicyRepository;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(TravelInsuranceService.class);
    

    private Double calculateTravelInsurancePrice(TravelInsurance insurance) {
        Long currentInsurancePolicyId = insurance.getInsurancePolicy().getId();
        LocalDate startDate = insurancePolicyRepository.findById(currentInsurancePolicyId).get().getStartDate();
        LocalDate endDate = insurancePolicyRepository.findById(currentInsurancePolicyId).get().getEndDate();

        long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        Double longevityMultiplier = daysDifference / 365.0;

        Double basePrice = 100.0;

        Double typeMultiplier = switch (insurance.getTravelType()) {
            case BUSINESS -> 1.5;
            case LEISURE -> 1.2;
            case STUDENT -> 1.1;
        };

        Double areaMultiplier = switch (insurance.getCoverageArea()) {
            case EUROPE_UK -> 1.0;
            case WORLDWIDE_EXCLUDING_USA_CANADA -> 1.3;
            case WORLDWIDE -> 1.5;
        };

        return basePrice * typeMultiplier * areaMultiplier * longevityMultiplier;
    }

    private Double calculateCoverageAmount(TravelInsurance insurance) {
        return switch (insurance.getCoverageArea()) {
            case EUROPE_UK -> 100000.0;
            case WORLDWIDE_EXCLUDING_USA_CANADA -> 200000.0;
            case WORLDWIDE -> 300000.0;
        };
    }

    @Transactional
    @Cacheable
    public TravelInsuranceDto getTravelInsuranceById(Long id) {
        return travelInsuranceMapper.toDto(travelInsuranceRepository.findById(id).orElseThrow(() -> new TravelInsuranceNotFoundException("Travel insurance with id " + id + " not found")));
    }

    @Transactional
    public TravelInsuranceDto createTravelInsurance(TravelInsuranceCreationDto travelInsuranceDto) {
        TravelInsurance travelInsurance = travelInsuranceMapper.toEntity(travelInsuranceDto);

        if(travelInsuranceRepository.existsByInsurancePolicyId(travelInsuranceDto.insurancePolicyId())){
            throw new TravelInsuranceAlreadyExistsException("Travel insurance for policy with id " + travelInsuranceDto.insurancePolicyId() + " already exists");
        }
        travelInsurance.setCoverageAmount(calculateCoverageAmount(travelInsurance));
        travelInsurance = travelInsuranceRepository.save(travelInsurance);

        InsurancePolicy insurancePolicy = travelInsurance.getInsurancePolicy();
        if (insurancePolicy != null) {
            Double updatedPrice = calculateTravelInsurancePrice(travelInsurance);

            insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
            insurancePolicyRepository.updateStatusById(InsuranceStatus.valueOf("ACTIVE"), insurancePolicy.getId());
        }

        travelInsurance.setCoverageAmount(calculateCoverageAmount(travelInsurance));
        return travelInsuranceMapper.toDto(travelInsurance);

        // return travelInsuranceMapper.toDto(travelInsuranceRepository.save(travelInsuranceMapper.toEntity(travelInsuranceDto)));
    }

    @Transactional
    public TravelInsuranceDto updateTravelInsuranceByPolicyId(Long policyId, TravelInsuranceDto travelInsuranceDto) throws MessagingException, UnsupportedEncodingException {
        logger.info("Updating travel insurance for policy ID: {}", policyId);

        TravelInsurance existingTravelInsurance = travelInsuranceRepository.findByInsurancePolicyId(policyId)
                .orElseThrow(() -> {
                    logger.error("Travel insurance with policy ID {} not found", policyId);
                    return new TravelInsuranceNotFoundException("Travel insurance with policy ID " + policyId + " not found");
                });

        travelInsuranceMapper.partialUpdate(travelInsuranceDto, existingTravelInsurance);
        existingTravelInsurance.setCoverageAmount(calculateCoverageAmount(existingTravelInsurance));

        TravelInsurance updatedTravelInsurance = travelInsuranceRepository.save(existingTravelInsurance);

        logger.info("Travel insurance updated successfully: {}", updatedTravelInsurance);

        InsurancePolicy insurancePolicy = updatedTravelInsurance.getInsurancePolicy();
        Double updatedPrice = 0.0;
        if (insurancePolicy != null) {
            logger.info("Updating price and status for insurance policy ID: {}", insurancePolicy.getId());
            updatedPrice = calculateTravelInsurancePrice(updatedTravelInsurance);
            insurancePolicyRepository.updatePriceById(updatedPrice, insurancePolicy.getId());
            insurancePolicyRepository.updateStatusById(InsuranceStatus.valueOf("ACTIVE"), insurancePolicy.getId());
        } else {
            logger.warn("No insurance policy found for updated travel insurance ID: {}", updatedTravelInsurance.getId());
        }

        Double price = updatedPrice;

        String coverageArea = updatedTravelInsurance.getCoverageArea().toString();
        String destination = updatedTravelInsurance.getDestination();
        String travelType = updatedTravelInsurance.getTravelType().toString();
        Double coverageAmount = updatedTravelInsurance.getCoverageAmount();

        String body = "<html><body>" +
                "<h2>Dear " + insurancePolicy.getUser().getFirstName() + ",</h2>" +
                "<p>Your <strong>travel insurance</strong> has been successfully purchased. Here are the details:</p>" +
                "<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse; width: 50%;'>" +
                "<tr><td><strong>Coverage Area</strong></td><td>" + coverageArea + "</td></tr>" +
                "<tr><td><strong>Destination</strong></td><td>" + destination + "</td></tr>" +
                "<tr><td><strong>Travel Type</strong></td><td>" + travelType + "</td></tr>" +
                "<tr><td><strong>Coverage Amount</strong></td><td>$" + coverageAmount + "</td></tr>" +
                "<tr><td><strong>Price</strong></td><td>₴" + (Double)price + "</td></tr>" +
                "</table>" +
                "<p>Thank you for choosing us!</p>" +
                "</body></html>";

        String email = insurancePolicy.getUser().getEmail();
        emailService.sendEmail(email, insurancePolicy.getInsuranceType().toString(), body);

        return travelInsuranceMapper.toDto(updatedTravelInsurance);

    }

    @Transactional
    public TravelInsuranceDto getTravelInsuranceByPolicyId(Long policy){
        return travelInsuranceMapper.toDto(travelInsuranceRepository.findByInsurancePolicyId(policy).orElseThrow(() -> new TravelInsuranceNotFoundException("Travel insurance with policy ID " + policy + " not found")));
    }

    @Transactional
    public TravelInsuranceDto updateTravelInsurance(TravelInsuranceDto travelInsuranceDto) {
        return travelInsuranceMapper.toDto(travelInsuranceRepository.save(travelInsuranceMapper.toEntity(travelInsuranceDto)));
    }

    @Transactional
    @CacheEvict
    public void deleteTravelInsuranceById(Long id) {
        travelInsuranceRepository.deleteById(id);
    }

    @Transactional
    @Cacheable
    public Page<TravelInsuranceDto> getAllTravels(Pageable pageable) {
        return travelInsuranceRepository.findAll(pageable).map(travelInsuranceMapper::toDto);
    }

    @Transactional
    public Page<TravelInsuranceDto> getSortedTravels(String sortBy, String order, Pageable pageable) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<TravelInsurance> travelsPage = travelInsuranceRepository.findAll(sortedPageable);
        return travelsPage.map(travelInsuranceMapper::toDto);
    }

    @Transactional
    public Page<TravelInsuranceDto> getFilteredTravels(Long id, String travelType, String coverageArea, String destination, Pageable pageable) {
        Specification<TravelInsurance> specification = Specification.where(null);

        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + id + "%"));
        }
        if(travelType != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("travelType")), "%" + travelType.toLowerCase() + "%"));
        }
        if (coverageArea != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("coverageArea"), "%" + coverageArea.toLowerCase() + "%"));
        }
        if(coverageArea != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("coverageArea")), "%" + coverageArea.toLowerCase() + "%"));
        }
        if(destination != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("destination")), "%" + destination.toLowerCase() + "%"));
        }

        Page<TravelInsurance> travels = travelInsuranceRepository.findAll(specification, pageable);
        return travels.map(travelInsuranceMapper::toDto);
    }
}
