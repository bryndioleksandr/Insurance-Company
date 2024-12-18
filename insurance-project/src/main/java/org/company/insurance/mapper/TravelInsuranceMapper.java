package org.company.insurance.mapper;

import org.company.insurance.dto.TravelInsuranceCreationDto;
import org.company.insurance.dto.TravelInsuranceDto;
import org.company.insurance.entity.TravelInsurance;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TravelInsuranceMapper {
    TravelInsurance toEntity(TravelInsuranceDto travelInsuranceDto);

    TravelInsuranceDto toDto(TravelInsurance travelInsurance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TravelInsurance partialUpdate(TravelInsuranceDto travelInsuranceDto, @MappingTarget TravelInsurance travelInsurance);

    @Mapping(source = "coverageArea", target = "coverageArea")
    @Mapping(source = "destination", target = "destination")
    @Mapping(source = "travelType", target = "travelType")
    @Mapping(source = "insurancePolicyId", target = "insurancePolicy.id")
    TravelInsurance toEntity(TravelInsuranceCreationDto travelInsuranceCreationDto);
}