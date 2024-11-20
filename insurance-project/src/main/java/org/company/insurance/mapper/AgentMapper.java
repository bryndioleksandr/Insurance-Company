package org.company.insurance.mapper;

import org.company.insurance.dto.AgentCreationDto;
import org.company.insurance.dto.UserDto;
import org.company.insurance.entity.Agent;
import org.company.insurance.dto.AgentDto;
import org.company.insurance.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AgentMapper {

    @Mapping(target = "hireDate", source = "hireDate")
    @Mapping(target = "position", source = "position")
    Agent toEntity(AgentDto agentDto);

    AgentDto toDto(Agent agent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Agent partialUpdate(AgentDto agentDto, @MappingTarget Agent agent);

    @Mapping(source = "userId", target = "userId.id")
    Agent toEntity(AgentCreationDto agentCreationDto);

    UserDto toDto(User user);

}