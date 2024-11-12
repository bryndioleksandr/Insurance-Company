package org.company.insurance.mapper;

import org.company.insurance.dto.AutoInsuranceCreationDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.company.insurance.entity.AutoInsurance;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AutoInsuranceMapper {
    AutoInsurance toEntity(AutoInsuranceDto autoInsuranceDto);

    AutoInsuranceDto toDto(AutoInsurance autoInsurance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AutoInsurance partialUpdate(AutoInsuranceDto autoInsuranceDto, @MappingTarget AutoInsurance autoInsurance);

    AutoInsurance toEntity(AutoInsuranceCreationDto autoInsuranceCreationDto);

    AutoInsuranceCreationDto toDto1(AutoInsurance autoInsurance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AutoInsurance partialUpdate(AutoInsuranceCreationDto autoInsuranceCreationDto, @MappingTarget AutoInsurance autoInsurance);
}