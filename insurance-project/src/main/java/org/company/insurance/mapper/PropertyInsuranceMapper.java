package org.company.insurance.mapper;

import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.entity.PropertyInsurance;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PropertyInsuranceMapper {
    PropertyInsurance toEntity(PropertyInsuranceDto propertyInsuranceDto);

    PropertyInsuranceDto toDto(PropertyInsurance propertyInsurance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PropertyInsurance partialUpdate(PropertyInsuranceDto propertyInsuranceDto, @MappingTarget PropertyInsurance propertyInsurance);
}