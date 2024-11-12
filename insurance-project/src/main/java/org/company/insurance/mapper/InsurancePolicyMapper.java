package org.company.insurance.mapper;

import org.company.insurance.dto.InsurancePolicyCreationDto;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.entity.InsurancePolicy;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InsurancePolicyMapper {
    InsurancePolicy toEntity(InsurancePolicyDto insurancePolicyDto);

    InsurancePolicyDto toDto(InsurancePolicy insurancePolicy);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InsurancePolicy partialUpdate(InsurancePolicyDto insurancePolicyDto, @MappingTarget InsurancePolicy insurancePolicy);

    InsurancePolicy toEntity(InsurancePolicyCreationDto insurancePolicyCreationDto);
}