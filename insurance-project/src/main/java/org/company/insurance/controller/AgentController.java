package org.company.insurance.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.AgentCreationDto;
import org.company.insurance.dto.AgentDto;
import org.company.insurance.dto.AgentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.company.insurance.service.AgentService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/agents")
@AllArgsConstructor
public class AgentController {
    private final AgentService agentService;

    @GetMapping("{id}")
    public ResponseEntity<AgentDto> getAgentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(agentService.getAgentById(id));
    }

    @PostMapping
    public ResponseEntity<AgentDto> createAgent(@Valid @RequestBody AgentCreationDto agentCreationDto) {
        return ResponseEntity.ok(agentService.createAgent(agentCreationDto));
    }

    @PutMapping
    public ResponseEntity<AgentDto> updateAgent(@RequestBody AgentDto agentDto) {
        return ResponseEntity.ok(agentService.updateAgent(agentDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAgentById(@PathVariable("id") Long id) {
        agentService.deleteAgentById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllAgents(Pageable pageable) {
        Page<AgentDto> agentDtos = agentService.getAllAgents(pageable);
        if (agentDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(agentDtos, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedAgents(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<AgentDto> agentDtos = agentService.getSortedAgents(sortBy, order, pageable);
        if (agentDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(agentDtos, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredAgents(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "hireDate", required = false) LocalDate hireDate,
            @RequestParam(name = "position", required = false) String position,
            @PageableDefault Pageable pageable) {
        Page<AgentDto> agentDtos = agentService.getFilteredAgents(id, userId, hireDate, position, pageable);

        if (agentDtos.isEmpty()) {
            return new ResponseEntity<>("No agents found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(agentDtos, HttpStatus.OK);
    }
}
