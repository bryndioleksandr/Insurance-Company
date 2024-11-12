package org.company.insurance.mapper;

import org.company.insurance.dto.InsuranceCreationDto;
import org.company.insurance.dto.InsuranceDto;
import org.company.insurance.entity.Insurance;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InsuranceMapper {
    Insurance toEntity(InsuranceDto insuranceDto);

    InsuranceDto toDto(Insurance insurance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Insurance partialUpdate(InsuranceDto insuranceDto, @MappingTarget Insurance insurance);

    Insurance toEntity(InsuranceCreationDto insuranceCreationDto);
}