package org.company.insurance.mapper;

import org.company.insurance.dto.ClaimCreationDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.entity.Claim;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClaimMapper {
    Claim toEntity(ClaimDto claimDto);

    ClaimDto toDto(Claim claim);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Claim partialUpdate(ClaimDto claimDto, @MappingTarget Claim claim);

    Claim toEntity(ClaimCreationDto claimCreationDto);

    ClaimCreationDto toDto1(Claim claim);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Claim partialUpdate(ClaimCreationDto claimCreationDto, @MappingTarget Claim claim);
}