package org.company.insurance.service;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.AgentDto;
import org.company.insurance.dto.UserCreationDto;
import org.company.insurance.dto.UserDto;
import org.company.insurance.entity.Agent;
import org.company.insurance.entity.User;
import org.company.insurance.enums.Role;
import org.company.insurance.exception.AgentNotFoundException;
import org.company.insurance.exception.UserNotFoundException;
import org.company.insurance.mapper.UserMapper;
import org.company.insurance.repository.AgentRepository;
import org.company.insurance.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private AgentRepository agentRepository;

    public UserDto getUserById(Long id) {

        return userMapper.toDto(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found")));
    }

    public UserDto createUser(UserCreationDto userDto) {
        User user = userMapper.toEntity(userDto);

        User savedUser = userRepository.save(user);
        if (user.getRole() == Role.AGENT) {
            Agent agent = new Agent();
            agent.setUserId(savedUser);
            agent.setHireDate(userDto.hireDate());
            agent.setPosition(userDto.position());
            agentRepository.save(agent);
        }

        return userMapper.toDto(savedUser);
    }

    public UserDto updateUser(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

//    public Page<AgentDto> getAllAgents(int page, int size, String[] sort) {
//        return agentRepository.findAll(PageRequest.of(page, size, Sort.by(sort))).map(agentMapper::toDto);
//    }

    @Transactional
    public Page<UserDto> getSortedUsers(String sortBy, String order, Pageable pageable) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<User> usersPage = userRepository.findAll(sortedPageable);
        return usersPage.map(userMapper::toDto);
    }

//    @Transactional
//    public AgentDto getAgentWithUser(Long agentId) {
//        Agent agent = agentRepository.findById(agentId)
//                .orElseThrow(() -> new AgentNotFoundException("Agent with ID " + agentId + " not found"));
//
//        return agentMapper.toDto(agent);
//    }

    @Transactional
    public Page<UserDto> getFilteredUsers(User user, Pageable pageable) {
        Specification<User> specification = Specification.where(null);

        if (user.getId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("id"), "%" + user.getId() + "%"));
        }
        if(user.getBirthDate() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("birthDate")), "%" + user.getBirthDate() + "%"));
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

        Page<User> users = userRepository.findAll(specification, pageable);
        return users.map(userMapper::toDto);
    }
}
