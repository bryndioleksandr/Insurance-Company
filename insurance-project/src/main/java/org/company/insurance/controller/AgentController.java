package org.company.insurance.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.AgentCreationDto;
import org.company.insurance.dto.AgentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.company.insurance.service.AgentService;

@RestController
@RequestMapping("/api/agents")
@AllArgsConstructor
public class AgentController {
    private final AgentService agentService;

    @GetMapping("{id}")
    public ResponseEntity<AgentDto> getAgentById(@PathVariable Long id){
        return ResponseEntity.ok(agentService.getAgentById(id));
    }

    @PostMapping
    public ResponseEntity<AgentDto> createAgent(@Valid @RequestBody AgentCreationDto agentCreationDto){
        return ResponseEntity.ok(agentService.createAgent(agentCreationDto));
    }
}
