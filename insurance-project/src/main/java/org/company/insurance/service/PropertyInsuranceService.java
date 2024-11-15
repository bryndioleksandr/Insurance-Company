package org.company.insurance.service;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.PropertyInsuranceCreationDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.mapper.PropertyInsuranceMapper;
import org.company.insurance.repository.PropertyInsuranceRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyInsuranceService {
    private final PropertyInsuranceRepository propertyInsuranceRepository;
    private final PropertyInsuranceMapper propertyInsuranceMapper;

    public PropertyInsuranceDto getPropertyInsuranceById(Long id) {
        return propertyInsuranceMapper.toDto(propertyInsuranceRepository.findById(id).orElse(null));
    }

    public PropertyInsuranceDto createPropertyInsurance(PropertyInsuranceCreationDto propertyInsuranceDto) {
        return propertyInsuranceMapper.toDto(propertyInsuranceRepository.save(propertyInsuranceMapper.toEntity(propertyInsuranceDto)));
    }

    public PropertyInsuranceDto updatePropertyInsurance(PropertyInsuranceDto propertyInsuranceDto) {
        return propertyInsuranceMapper.toDto(propertyInsuranceRepository.save(propertyInsuranceMapper.toEntity(propertyInsuranceDto)));
    }

    public void deletePropertyInsuranceById(Long id) {
        propertyInsuranceRepository.deleteById(id);
    }
}
