package org.company.insurance.service;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.AgentCreationDto;
import org.company.insurance.dto.AgentDto;
import org.company.insurance.entity.Agent;
import org.company.insurance.entity.User;
import org.company.insurance.exception.AgentAlreadyExistsException;
import org.company.insurance.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.company.insurance.exception.AgentNotFoundException;
import org.company.insurance.repository.AgentRepository;
import org.company.insurance.mapper.AgentMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@CacheConfig(cacheResolver = "multiLevelCacheResolver")
public class AgentService {
    private AgentRepository agentRepository;
    private AgentMapper agentMapper;
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);


    @Transactional
    @Cacheable
    public AgentDto getAgentById(Long id) {
        logger.info("Fetching agent by ID: {}", id);
        return agentMapper.toDto(agentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Agent with ID {} not found", id);
                    return new AgentNotFoundException("Agent with id " + id + " not found");
                }));
    }

    @Transactional
    public AgentDto createAgent(AgentCreationDto agentDto) {
        logger.info("Creating a new agent");
        Agent agentEntity = agentMapper.toEntity(agentDto);
        Agent savedAgent = agentRepository.save(agentEntity);
        logger.info("Agent created: {}", savedAgent);
        return agentMapper.toDto(savedAgent);
    }

    public AgentDto updateAgent(AgentDto agentDto) {
        logger.info("Updating agent with ID: {}", agentDto.id());
        if (!agentRepository.existsById(agentDto.id())) {
            logger.error("Agent with ID {} not found", agentDto.id());
            throw new AgentNotFoundException("Agent with id " + agentDto.id() + " not found");
        }
        return agentMapper.toDto(agentRepository.save(agentMapper.toEntity(agentDto)));
    }

    @CacheEvict
    public void deleteAgentById(Long id) {
        logger.info("Deleting agent with ID: {}", id);
        if (!agentRepository.existsById(id)) {
            logger.error("Agent with ID {} not found", id);
            throw new AgentNotFoundException("Agent with id " + id + " not found");
        }
        agentRepository.deleteById(id);
        logger.info("Agent with ID {} deleted", id);
    }

    @Transactional
    @Cacheable
    public Page<AgentDto> getAllAgents(Pageable pageable) {
        logger.info("Fetching all agents with pagination: {}", pageable);
        return agentRepository.findAll(pageable).map(agentMapper::toDto);
    }

    @Transactional
    public Page<AgentDto> getSortedAgents(String sortBy, String order, Pageable pageable) {
        logger.info("Fetching sorted agents by {} in {} order", sortBy, order);
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return agentRepository.findAll(sortedPageable).map(agentMapper::toDto);
    }

    @Transactional
    public Page<AgentDto> getFilteredAgents(Long id, Long userId, LocalDate hireDate, String position, Pageable pageable) {
        logger.info("Fetching filtered agents with criteria: id={}, userId={}, hireDate={}, position={}", id, userId, hireDate, position);
        Specification<Agent> specification = Specification.where(null);

        if (hireDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("hireDate")), "%" + hireDate + "%"));
        }
        if (position != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("position")), "%" + position.toLowerCase() + "%"));
        }
        if (userId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("userId"), "%" + userId + "%"));
        }
        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + id + "%"));
        }

        Page<Agent> agents = agentRepository.findAll(specification, pageable);
        return agents.map(agentMapper::toDto);
    }
}
