package org.company.insurance.mapper;

import org.company.insurance.dto.ClaimAssessmentDto;
import org.company.insurance.entity.ClaimAssessment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClaimAssessmentMapper {
    ClaimAssessment toEntity(ClaimAssessmentDto claimAssessmentDto);

    ClaimAssessmentDto toDto(ClaimAssessment claimAssessment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClaimAssessment partialUpdate(ClaimAssessmentDto claimAssessmentDto, @MappingTarget ClaimAssessment claimAssessment);
}