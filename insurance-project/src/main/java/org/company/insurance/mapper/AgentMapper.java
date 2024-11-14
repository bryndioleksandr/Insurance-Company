package org.company.insurance.mapper;

import org.company.insurance.dto.AgentCreationDto;
import org.company.insurance.entity.Agent;
import org.company.insurance.dto.AgentDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AgentMapper {

    @Mapping(target = "hireDate", source = "hireDate")
    @Mapping(target = "position", source = "position")
    Agent toEntity(AgentDto agentDto);

    AgentDto toDto(Agent agent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Agent partialUpdate(AgentDto agentDto, @MappingTarget Agent agent);

    Agent toEntity(AgentCreationDto agentCreationDto);

}