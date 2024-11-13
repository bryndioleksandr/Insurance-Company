package org.company.insurance.service;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.AgentDto;
import org.company.insurance.repository.AgentRepository;
import org.company.insurance.mapper.AgentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AgentService {
    private AgentRepository agentRepository;
    private AgentMapper agentMapper;

    public List<AgentDto> getAllAgents() {
        return agentMapper.toDto(agentRepository.findAll());
    }
}
