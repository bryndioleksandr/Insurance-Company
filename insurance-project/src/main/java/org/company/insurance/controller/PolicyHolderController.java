package org.company.insurance.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.PolicyHolderCreationDto;
import org.company.insurance.dto.PolicyHolderDto;

import org.company.insurance.service.PolicyHolderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policy-holders")
@AllArgsConstructor
public class PolicyHolderController {
    private final PolicyHolderService policyHolderService;

    @Operation(
            summary = "Get policy holder by ID",
            description = "Fetches a policy holder by their unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Policy holder found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PolicyHolderDto.class))),
                    @ApiResponse(responseCode = "404", description = "Policy holder not found")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("{id}")
    public ResponseEntity<PolicyHolderDto> getPolicyHolderById(@PathVariable Long id){
        return ResponseEntity.ok(policyHolderService.getPolicyHolderById(id));
    }

    @Operation(
            summary = "Create a new policy holder",
            description = "Creates a new policy holder by providing necessary details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Policy holder created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PolicyHolderDto.class)))
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<PolicyHolderDto> createPolicyHolder(@RequestBody PolicyHolderCreationDto policyHolderDto){
        return ResponseEntity.ok(policyHolderService.createPolicyHolder(policyHolderDto));
    }

    @Operation(
            summary = "Update an existing policy holder",
            description = "Updates details of an existing policy holder",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Policy holder updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PolicyHolderDto.class)))
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<PolicyHolderDto> updatePolicyHolder(@RequestBody PolicyHolderDto policyHolderDto) {
        return ResponseEntity.ok(policyHolderService.updatePolicyHolder(policyHolderDto));
    }

    @Operation(
            summary = "Delete a policy holder by ID",
            description = "Deletes a policy holder from the system using their unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Policy holder deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Policy holder not found")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePolicyHolderById(@PathVariable("id") Long id) {
        policyHolderService.deletePolicyHolderById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all policy holders",
            description = "Fetches a paginated list of all policy holders",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of policy holders",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PolicyHolderDto.class))),
                    @ApiResponse(responseCode = "204", description = "No policy holders found")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getAllHolders(Pageable pageable) {
        Page<PolicyHolderDto> policyHolderDtos = policyHolderService.getAllHolders(pageable);
        if(policyHolderDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(policyHolderDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get sorted policy holders",
            description = "Fetches a paginated list of policy holders sorted by a specified field",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of sorted policy holders",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PolicyHolderDto.class))),
                    @ApiResponse(responseCode = "204", description = "No policy holders found")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedHolders(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<PolicyHolderDto> policyHolderDtos = policyHolderService.getSortedHolders(sortBy, order, pageable);
        if(policyHolderDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(policyHolderDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered policy holders",
            description = "Fetches a filtered list of policy holders based on the provided query parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered list of policy holders",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PolicyHolderDto.class))),
                    @ApiResponse(responseCode = "404", description = "No policy holders found matching the filters")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredHolders (
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "passportNumber", required = false) String passportNumber,
            @RequestParam(name = "address", required = false) String address,
            @PageableDefault Pageable pageable
    ) {
        Page<PolicyHolderDto> policyHolderDtos = policyHolderService.getFilteredHolders(id, passportNumber, address, pageable);

        if (policyHolderDtos.isEmpty()) {
            return new ResponseEntity<>("No policyHolders found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(policyHolderDtos, HttpStatus.OK);
    }
}
