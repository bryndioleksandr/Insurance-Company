package org.company.insurance.mapper;

import jakarta.persistence.*;
import org.company.insurance.dto.ClaimCreationDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.entity.Claim;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.enums.ClaimType;
import org.company.insurance.enums.Status;
import org.mapstruct.*;

import java.time.LocalDate;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClaimMapper {
    Claim toEntity(ClaimDto claimDto);

    ClaimDto toDto(Claim claim);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Claim partialUpdate(ClaimDto claimDto, @MappingTarget Claim claim);

    @Mapping(source = "submissionDate", target = "submissionDate")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "insurancePolicyId", target = "insurancePolicy.id")
    @Mapping(source = "incidentDescription", target = "incidentDescription")
    @Mapping(source = "claimType", target = "claimType")
    Claim toEntity(ClaimCreationDto claimCreationDto);

    ClaimCreationDto toDto1(Claim claim);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Claim partialUpdate(ClaimCreationDto claimCreationDto, @MappingTarget Claim claim);
}