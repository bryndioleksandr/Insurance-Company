package org.company.insurance.service;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.AgentCreationDto;
import org.company.insurance.dto.AgentDto;
import org.company.insurance.entity.Agent;
import org.company.insurance.entity.User;
import org.company.insurance.exception.AgentAlreadyExistsException;
import org.company.insurance.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private UserRepository userRepository;

    @Transactional
    public AgentDto getAgentById(Long id) {
        return agentMapper.toDto(agentRepository.findById(id)
                .orElseThrow(() -> new AgentNotFoundException("Agent with id " + id + " not found")));
    }

    @Transactional
    public AgentDto createAgent(AgentCreationDto agentDto) {
        Agent agentEntity = agentMapper.toEntity(agentDto);
        System.out.println("Agent entity before save: " + agentEntity);
        Agent savedAgent = agentRepository.save(agentEntity);
        System.out.println("Creating agent: " + savedAgent);
        return agentMapper.toDto(savedAgent);
    }

    public AgentDto updateAgent(AgentDto agentDto) {
        if (!agentRepository.existsById(agentDto.id())) {
            throw new AgentNotFoundException("Agent with id " + agentDto.id() + " not found");
        }
            return agentMapper.toDto(agentRepository.save(agentMapper.toEntity(agentDto)));
    }

    public void deleteAgentById(Long id) {
        if (!agentRepository.existsById(id)) {
            throw new AgentNotFoundException("Agent with id " + id + " not found");
        }
        agentRepository.deleteById(id);
    }

    @Transactional
    public Page<AgentDto> getAllAgents(Pageable pageable) {
        return agentRepository.findAll(pageable).map(agentMapper::toDto);
    }

//    public Page<AgentDto> getAllAgents(int page, int size, String[] sort) {
//        return agentRepository.findAll(PageRequest.of(page, size, Sort.by(sort))).map(agentMapper::toDto);
//    }

    @Transactional
    public Page<AgentDto> getSortedAgents(String sortBy, String order, Pageable pageable) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Agent> agentsPage = agentRepository.findAll(sortedPageable);
        return agentsPage.map(agentMapper::toDto);
    }

//    @Transactional
//    public AgentDto getAgentWithUser(Long agentId) {
//        Agent agent = agentRepository.findById(agentId)
//                .orElseThrow(() -> new AgentNotFoundException("Agent with ID " + agentId + " not found"));
//
//        return agentMapper.toDto(agent);
//    }

    @Transactional
    public Page<AgentDto> getFilteredAgents(Agent agent, Pageable pageable) {
        Specification<Agent> specification = Specification.where(null);
        User user = userRepository.findById(agent.getUserId().getId()).orElseThrow(() -> new AgentNotFoundException("User with id " + agent.getUserId().getId() + " not found"));

        if (agent.getHireDate() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("hireDate")), "%" + agent.getHireDate() + "%"));
        }
        if (agent.getPosition() != null && !agent.getPosition().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("position")), "%" + agent.getPosition().toLowerCase() + "%"));
        }
        if (agent.getId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + agent.getId() + "%"));
        }
        if (user.getPhoneNumber() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + user.getPhoneNumber() + "%"));
        }
        if(user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + user.getFirstName().toLowerCase() + "%"));
        }
        if(user.getSurname() != null && !user.getSurname().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + user.getSurname().toLowerCase() + "%"));
        }
        if(user.getEmail() != null && !user.getEmail().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + user.getEmail().toLowerCase() + "%"));
        }
        if(user.getRole() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("role")), "%" + user.getRole().toString().toLowerCase() + "%"));
        }

        Page<Agent> agents = agentRepository.findAll(specification, pageable);
        return agents.map(agentMapper::toDto);
    }
}
