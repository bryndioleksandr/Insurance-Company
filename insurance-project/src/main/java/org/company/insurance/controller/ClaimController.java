package org.company.insurance.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.ClaimCreationDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.service.ClaimService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/claims")
@AllArgsConstructor
public class ClaimController {
    private final ClaimService claimService;
    @Operation(
            summary = "Get claim by ID",
            description = "Fetches the claim with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Claim found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimDto.class))),
                    @ApiResponse(responseCode = "404", description = "Claim not found")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<ClaimDto> getClaimById(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    @Operation(
            summary = "Create a new claim",
            description = "Creates a new claim with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Claim created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ClaimDto> createClaim(@RequestBody ClaimCreationDto claimCreationDto) {
        return ResponseEntity.ok(claimService.createClaim(claimCreationDto));
    }

    @Operation(
            summary = "Update an existing claim",
            description = "Updates an existing claim with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Claim updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Claim not found")
            }
    )
    @PutMapping
    public ResponseEntity<ClaimDto> updateClaim(@RequestBody ClaimDto claimDto) {
        return ResponseEntity.ok(claimService.updateClaim(claimDto));
    }

    @Operation(
            summary = "Delete claim by ID",
            description = "Deletes the claim with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Claim deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Claim not found")
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClaimById(@PathVariable("id") Long id) {
        claimService.deleteClaimById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all claims",
            description = "Fetches all claims with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of claims",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimDto.class))),
                    @ApiResponse(responseCode = "204", description = "No content")
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllClaims(Pageable pageable) {
        Page<ClaimDto> claimDtos = claimService.getAllClaims(pageable);
        if (claimDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(claimDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get sorted claims",
            description = "Fetches sorted claims based on the provided sorting parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sorted list of claims",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimDto.class))),
                    @ApiResponse(responseCode = "204", description = "No content")
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedClaims(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<ClaimDto> claimDtos = claimService.getSortedClaims(sortBy, order, pageable);
        if (claimDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(claimDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered claims",
            description = "Fetches a filtered list of claims based on the provided query parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered list of claims",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimDto.class))),
                    @ApiResponse(responseCode = "404", description = "No claims found matching the filters")
            }
    )
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredClaims(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "submissionDate", required = false) LocalDate submissionDate,
            @RequestParam(name = "amount", required = false) double amount,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "insurancePolicy", required = false) Long insurancePolicy,
            @RequestParam(name = "incidentDescription", required = false) String incidentDescription,
            @RequestParam(name = "claimType", required = false) String claimType,
            @PageableDefault Pageable pageable) {
        Page<ClaimDto> claimDtos = claimService.getFilteredClaims(id, submissionDate, amount, status, insurancePolicy, incidentDescription, claimType, pageable);

        if (claimDtos.isEmpty()) {
            return new ResponseEntity<>("No claims found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(claimDtos, HttpStatus.OK);
    }
}
