package org.company.insurance.mapper;

import org.company.insurance.dto.InsurancePolicyCreationDto;
import org.company.insurance.dto.InsurancePolicyCreationDto_;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.entity.InsurancePolicy;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InsurancePolicyMapper {
    InsurancePolicy toEntity(InsurancePolicyDto insurancePolicyDto);

    @InheritInverseConfiguration(name = "toEntity")

    InsurancePolicyDto toDto(InsurancePolicy insurancePolicy);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InsurancePolicy partialUpdate(InsurancePolicyDto insurancePolicyDto, @MappingTarget InsurancePolicy insurancePolicy);


    @Mapping(source = "policyHolderId", target = "policyHolder.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "status", target = "status")
    InsurancePolicy toEntity(InsurancePolicyCreationDto insurancePolicyCreationDto);


    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InsurancePolicy partialUpdate(InsurancePolicyCreationDto insurancePolicyCreationDto, @MappingTarget InsurancePolicy insurancePolicy);
}