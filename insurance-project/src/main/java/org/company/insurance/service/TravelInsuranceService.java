package org.company.insurance.service;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.TravelInsuranceCreationDto;
import org.company.insurance.dto.TravelInsuranceDto;
import org.company.insurance.mapper.TravelInsuranceMapper;
import org.company.insurance.repository.TravelInsuranceRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TravelInsuranceService {
    private final TravelInsuranceRepository travelInsuranceRepository;
    private final TravelInsuranceMapper travelInsuranceMapper;

    public TravelInsuranceDto getTravelInsuranceById(Long id) {
        return travelInsuranceMapper.toDto(travelInsuranceRepository.findById(id).orElse(null));
    }

    public TravelInsuranceDto createTravelInsurance(TravelInsuranceCreationDto travelInsuranceDto) {
        return travelInsuranceMapper.toDto(travelInsuranceRepository.save(travelInsuranceMapper.toEntity(travelInsuranceDto)));
    }

    public TravelInsuranceDto updateTravelInsurance(TravelInsuranceDto travelInsuranceDto) {
        return travelInsuranceMapper.toDto(travelInsuranceRepository.save(travelInsuranceMapper.toEntity(travelInsuranceDto)));
    }

    public void deleteTravelInsuranceById(Long id) {
        travelInsuranceRepository.deleteById(id);
    }
}
