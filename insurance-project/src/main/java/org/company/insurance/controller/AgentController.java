package org.company.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Get agent by ID",
            description = "Fetches an agent's details using their unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Agent found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentDto.class))),
                    @ApiResponse(responseCode = "404", description = "Agent not found")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<AgentDto> getAgentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(agentService.getAgentById(id));
    }

    @Operation(
            summary = "Create a new agent",
            description = "Creates a new agent with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Agent created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    public ResponseEntity<AgentDto> createAgent(@Valid @RequestBody AgentCreationDto agentCreationDto) {
        return ResponseEntity.ok(agentService.createAgent(agentCreationDto));
    }

    @Operation(
            summary = "Update agent details",
            description = "Updates an existing agent's details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Agent updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentDto.class))),
                    @ApiResponse(responseCode = "404", description = "Agent not found")
            }
    )
    @PutMapping
    public ResponseEntity<AgentDto> updateAgent(@RequestBody AgentDto agentDto) {
        return ResponseEntity.ok(agentService.updateAgent(agentDto));
    }

    @Operation(
            summary = "Delete agent by ID",
            description = "Deletes an agent using their unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Agent deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Agent not found")
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAgentById(@PathVariable("id") Long id) {
        agentService.deleteAgentById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all agents",
            description = "Fetches all agents with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of agents",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "204", description = "No agents found")
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllAgents(Pageable pageable) {
        Page<AgentDto> agentDtos = agentService.getAllAgents(pageable);
        if (agentDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(agentDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get sorted agents",
            description = "Fetches agents sorted by specified criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sorted list of agents",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "204", description = "No agents found")
            }
    )
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

    @Operation(
            summary = "Get filtered agents",
            description = "Fetches agents based on filter criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered list of agents",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "404", description = "No agents found")
            }
    )
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
