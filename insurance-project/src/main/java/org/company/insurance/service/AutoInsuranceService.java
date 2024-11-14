package org.company.insurance.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.AutoInsuranceCreationDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.company.insurance.repository.AutoInsuranceRepository;
import org.springframework.stereotype.Service;
import org.company.insurance.mapper.AutoInsuranceMapper;

@AllArgsConstructor
@Service
@Transactional
public class AutoInsuranceService {
    private AutoInsuranceRepository autoInsuranceRepository;
    private AutoInsuranceMapper autoInsuranceMapper;

    public AutoInsuranceDto getAutoInsuranceById(Long id) {
        return autoInsuranceMapper.toDto(autoInsuranceRepository.findById(id).orElse(null));
    }

    public AutoInsuranceDto createAutoInsurance(AutoInsuranceCreationDto autoInsuranceDto) {
        return autoInsuranceMapper.toDto(autoInsuranceRepository.save(autoInsuranceMapper.toEntity(autoInsuranceDto)));
    }

    public AutoInsuranceDto updateAutoInsurance(AutoInsuranceDto autoInsuranceDto) {
        return autoInsuranceMapper.toDto(autoInsuranceRepository.save(autoInsuranceMapper.toEntity(autoInsuranceDto)));
    }

    public void deleteAutoInsuranceById(Long id) {
        autoInsuranceRepository.deleteById(id);
    }
}
