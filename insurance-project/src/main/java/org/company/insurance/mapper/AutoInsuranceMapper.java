package org.company.insurance.mapper;

import jakarta.persistence.*;
import org.company.insurance.dto.AutoInsuranceCreationDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.company.insurance.entity.AutoInsurance;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.enums.AutoInsuranceType;
import org.company.insurance.enums.VehicleType;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AutoInsuranceMapper {
    //@Mapping(target = "insurancePolicy", source = "insurancePolicyId")
    @Mapping(source = "insurancePolicyId", target = "insurancePolicy.id")
    AutoInsurance toEntity(AutoInsuranceDto autoInsuranceDto);

    AutoInsuranceDto toDto(AutoInsurance autoInsurance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AutoInsurance partialUpdate(AutoInsuranceDto autoInsuranceDto, @MappingTarget AutoInsurance autoInsurance);

    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "model", target = "model")
    @Mapping(source = "year", target = "year")
    @Mapping(source = "plate", target = "plate")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "insuranceType", target = "insuranceType")
    @Mapping(source = "insurancePolicyId", target = "insurancePolicy.id")
    AutoInsurance toEntity(AutoInsuranceCreationDto autoInsuranceCreationDto);

    AutoInsuranceCreationDto toDto1(AutoInsurance autoInsurance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AutoInsurance partialUpdate(AutoInsuranceCreationDto autoInsuranceCreationDto, @MappingTarget AutoInsurance autoInsurance);

}