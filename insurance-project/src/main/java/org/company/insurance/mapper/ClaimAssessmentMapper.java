package org.company.insurance.mapper;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import org.company.insurance.dto.ClaimAssessmentCreationDto;
import org.company.insurance.dto.ClaimAssessmentDto;
import org.company.insurance.entity.Agent;
import org.company.insurance.entity.Claim;
import org.company.insurance.entity.ClaimAssessment;
import org.mapstruct.*;

import java.time.LocalDate;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClaimAssessmentMapper {
    ClaimAssessment toEntity(ClaimAssessmentDto claimAssessmentDto);

    ClaimAssessmentDto toDto(ClaimAssessment claimAssessment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClaimAssessment partialUpdate(ClaimAssessmentDto claimAssessmentDto, @MappingTarget ClaimAssessment claimAssessment);
    
    @Mapping(source = "claimId", target = "claim.id")
    @Mapping(source = "assessmentDate", target = "assessmentDate")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "assessmentAmount", target = "assessmentAmount")
    @Mapping(source = "agentId", target = "agent.id")
    ClaimAssessment toEntity(ClaimAssessmentCreationDto claimAssessmentCreationDto);

    ClaimAssessmentCreationDto toDto1(ClaimAssessment claimAssessment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClaimAssessment partialUpdate(ClaimAssessmentCreationDto claimAssessmentCreationDto, @MappingTarget ClaimAssessment claimAssessment);
}