package org.company.insurance.service;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.AgentCreationDto;
import org.company.insurance.dto.AgentDto;
import org.company.insurance.entity.Agent;
import org.company.insurance.exception.AgentAlreadyExistsException;
import org.company.insurance.exception.AgentNotFoundException;
import org.company.insurance.repository.AgentRepository;
import org.company.insurance.mapper.AgentMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AgentService {
    private AgentRepository agentRepository;
    private AgentMapper agentMapper;


    public AgentDto getAgentById(Long id) {
        return agentMapper.toDto(agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException("Agent with id " + id + " not found")));
    }

    public AgentDto createAgent(AgentCreationDto agentDto) {
        Agent agentEntity = agentMapper.toEntity(agentDto);
        System.out.println("Agent entity before save: " + agentEntity);
        Agent savedAgent = agentRepository.save(agentEntity);
        System.out.println("Creating agent: " + savedAgent);
        return agentMapper.toDto(savedAgent);
    }

    public AgentDto updateAgent(AgentDto agentDto) {
        return agentMapper.toDto(agentRepository.save(agentMapper.toEntity(agentDto)));
    }

    public void deleteAgentById(Long id) {
        agentRepository.deleteById(id);
    }

    public List<AgentDto> getAllAgents() {
        return agentRepository.findAll().stream()
                .map(agentMapper::toDto)
                .collect(Collectors.toList());
    }
}
