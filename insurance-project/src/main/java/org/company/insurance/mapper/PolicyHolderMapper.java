package org.company.insurance.mapper;

import org.company.insurance.dto.PolicyHolderCreationDto;
import org.company.insurance.dto.PolicyHolderDto;
import org.company.insurance.entity.PolicyHolder;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PolicyHolderMapper {
    @Mapping(target = "passportNumber", source = "passportNumber")
    @Mapping(target = "address", source = "address")
    PolicyHolder toEntity(PolicyHolderDto policyHolderDto);

    PolicyHolderDto toDto(PolicyHolder policyHolder);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PolicyHolder partialUpdate(PolicyHolderDto policyHolderDto, @MappingTarget PolicyHolder policyHolder);

    @Mapping(source = "userId", target = "userId.id")
    PolicyHolder toEntity(PolicyHolderCreationDto policyHolderCreationDto);
}