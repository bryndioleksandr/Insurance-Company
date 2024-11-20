package org.company.insurance.mapper;

import org.company.insurance.dto.HealthInsuranceCreationDto;
import org.company.insurance.dto.HealthInsuranceDto;
import org.company.insurance.entity.HealthInsurance;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HealthInsuranceMapper {
    HealthInsurance toEntity(HealthInsuranceDto healthInsuranceDto);

    HealthInsuranceDto toDto(HealthInsurance healthInsurance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    HealthInsurance partialUpdate(HealthInsuranceDto healthInsuranceDto, @MappingTarget HealthInsurance healthInsurance);

    @Mapping(source = "medicalHistory", target = "medicalHistory")
    @Mapping(source = "coverageAmount", target = "coverageAmount")
    @Mapping(source = "insurancePolicyId", target = "insurancePolicy.id")
    HealthInsurance toEntity(HealthInsuranceCreationDto healthInsuranceCreationDto);

    HealthInsuranceCreationDto toDto1(HealthInsurance healthInsurance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    HealthInsurance partialUpdate(HealthInsuranceCreationDto healthInsuranceCreationDto, @MappingTarget HealthInsurance healthInsurance);
}