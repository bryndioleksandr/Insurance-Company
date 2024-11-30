package org.company.insurance.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.company.insurance.dto.ClaimAssessmentCreationDto;
import org.company.insurance.dto.ClaimAssessmentDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.dto.ClaimAssessmentDto;
import org.company.insurance.service.ClaimAssessmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/claim-assessments")
@AllArgsConstructor
public class ClaimAssessmentController {
    private final ClaimAssessmentService claimAssessmentService;

    @Operation(
            summary = "Get claim assessment by ID",
            description = "Fetches a claim assessment by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Claim assessment found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimAssessmentDto.class))),
                    @ApiResponse(responseCode = "404", description = "Claim assessment not found")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("{id}")
    public ResponseEntity<ClaimAssessmentDto> getClaimAssessmentById(@PathVariable Long id) {
        return ResponseEntity.ok(claimAssessmentService.getClaimAssessmentById(id));
    }

    @Operation(
            summary = "Create a new claim assessment",
            description = "Creates a new claim assessment with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Claim assessment created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimAssessmentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ClaimAssessmentDto> createClaimAssessment(@RequestBody ClaimAssessmentCreationDto claimAssessmentDto) {
        return ResponseEntity.ok(claimAssessmentService.createClaimAssessment(claimAssessmentDto));
    }

    @Operation(
            summary = "Update an existing claim assessment",
            description = "Updates the details of an existing claim assessment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Claim assessment updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimAssessmentDto.class))),
                    @ApiResponse(responseCode = "404", description = "Claim assessment not found")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<ClaimAssessmentDto> updateClaimAssessment(@RequestBody ClaimAssessmentDto claimAssessmentDto) {
        return ResponseEntity.ok(claimAssessmentService.updateClaimAssessment(claimAssessmentDto));
    }

    @Operation(
            summary = "Delete a claim assessment by ID",
            description = "Deletes a claim assessment by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Claim assessment deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Claim assessment not found")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClaimAssessmentById(@PathVariable("id") Long id) {
        claimAssessmentService.deleteClaimAssessmentById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all claim assessments",
            description = "Fetches a paginated list of all claim assessments",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of claim assessments",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimAssessmentDto.class))),
                    @ApiResponse(responseCode = "204", description = "No claim assessments found")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllClaimAssessments(Pageable pageable) {
        Page<ClaimAssessmentDto> claimAssessmentDtos = claimAssessmentService.getAllClaimAssessments(pageable);
        if (claimAssessmentDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(claimAssessmentDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get sorted claim assessments",
            description = "Fetches a sorted list of claim assessments based on the provided sort parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sorted list of claim assessments",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimAssessmentDto.class))),
                    @ApiResponse(responseCode = "204", description = "No claim assessments found")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedClaimAssessments(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<ClaimAssessmentDto> claimAssessmentDtos = claimAssessmentService.getSortedClaimAssessments(sortBy, order, pageable);
        if (claimAssessmentDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(claimAssessmentDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered claim assessments",
            description = "Fetches a filtered list of claim assessments based on the provided query parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered list of claim assessments",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimAssessmentDto.class))),
                    @ApiResponse(responseCode = "404", description = "No claim assessments found matching the filters")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredClaimAssessments(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "assessmentDate", required = false) LocalDate assessmentDate,
            @RequestParam(name = "notes", required = false) String notes,
            @RequestParam(name = "assessmentAmount", required = false) Double assessmentAmount,
            @RequestParam(name = "agent", required = false) Long agent,
            @PageableDefault Pageable pageable) {
        Page<ClaimAssessmentDto> claimAssessmentDtos = claimAssessmentService.getFilteredClaimAssessments(id, assessmentDate, notes, assessmentAmount, agent, pageable);

        if (claimAssessmentDtos.isEmpty()) {
            return new ResponseEntity<>("No claim assessments found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(claimAssessmentDtos, HttpStatus.OK);
    }
}
