//package org.company.insurance.service;
//
//
//import lombok.AllArgsConstructor;
//import org.company.insurance.dto.InsuranceCreationDto;
//import org.company.insurance.dto.InsuranceDto;
//import org.company.insurance.mapper.InsuranceMapper;
//import org.company.insurance.repository.InsuranceRepository;
//import org.springframework.stereotype.Service;
//
//@AllArgsConstructor
//@Service
//public class InsuranceService {
//    private InsuranceRepository insuranceRepository;
//    private InsuranceMapper insuranceMapper;
//
//    public InsuranceDto getInsuranceById(Long id) {
//        return insuranceMapper.toDto(insuranceRepository.findById(id).orElse(null));
//    }
//
//    public InsuranceDto createInsurance(InsuranceCreationDto insuranceDto) {
//        return insuranceMapper.toDto(insuranceRepository.save(insuranceMapper.toEntity(insuranceDto)));
//    }
//
//    public InsuranceDto updateInsurance(InsuranceDto insuranceDto) {
//        return insuranceMapper.toDto(insuranceRepository.save(insuranceMapper.toEntity(insuranceDto)));
//    }
//
//    public void deleteInsuranceById(Long id) {
//        insuranceRepository.deleteById(id);
//    }
//}
