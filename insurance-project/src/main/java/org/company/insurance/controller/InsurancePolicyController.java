package org.company.insurance.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.InsurancePolicyCreationDto;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.entity.Insurance;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.service.InsurancePolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/insurance-policies")
@AllArgsConstructor
public class InsurancePolicyController {
    private final InsurancePolicyService insurancePolicyService;

    @Operation(
            summary = "Get insurance policy by ID",
            description = "Fetches the insurance policy with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Insurance policy found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InsurancePolicyDto.class))),
                    @ApiResponse(responseCode = "404", description = "Insurance policy not found")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<InsurancePolicyDto> getInsurancePolicyById(@PathVariable Long id) {
        return ResponseEntity.ok(insurancePolicyService.getInsurancePolicyById(id));
    }

    @Operation(
            summary = "Create a new insurance policy",
            description = "Creates a new insurance policy with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Insurance policy created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InsurancePolicyDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<InsurancePolicyDto> createInsurancePolicy(@RequestBody InsurancePolicyCreationDto insurancePolicy) {
        return ResponseEntity.ok(insurancePolicyService.createInsurancePolicy(insurancePolicy));
    }

    @Operation(
            summary = "Update an insurance policy",
            description = "Updates an existing insurance policy with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Insurance policy updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InsurancePolicyDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PutMapping
    public ResponseEntity<InsurancePolicyDto> updateInsurancePolicy(@RequestBody InsurancePolicyDto insurancePolicyDto) {
        return ResponseEntity.ok(insurancePolicyService.updateInsurancePolicy(insurancePolicyDto));
    }

    @Operation(
            summary = "Delete insurance policy by ID",
            description = "Deletes the insurance policy with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Insurance policy deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Insurance policy not found")
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteInsurancePolicyById(@PathVariable Long id) {
        insurancePolicyService.deleteInsurancePolicyById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all insurance policies",
            description = "Fetches a paginated list of all insurance policies",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of insurance policies",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InsurancePolicyDto.class))),
                    @ApiResponse(responseCode = "204", description = "No insurance policies found")
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllInsurances(Pageable pageable) {
        Page<InsurancePolicyDto> insurancePolicyDtos = insurancePolicyService.getAllInsurances(pageable);
        if (insurancePolicyDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(insurancePolicyDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get sorted insurance policies",
            description = "Fetches a sorted list of insurance policies based on provided parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sorted list of insurance policies",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InsurancePolicyDto.class))),
                    @ApiResponse(responseCode = "204", description = "No insurance policies found")
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedInsurances(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<InsurancePolicyDto> insurancePolicyDtos = insurancePolicyService.getSortedInsurances(sortBy, order, pageable);
        if (insurancePolicyDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(insurancePolicyDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered insurance policies",
            description = "Fetches a filtered list of insurance policies based on the provided query parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered list of insurance policies",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InsurancePolicyDto.class))),
                    @ApiResponse(responseCode = "404", description = "No insurance policies found matching the filters")
            }
    )
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredInsurances(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "policyNumber", required = false) String policyNumber,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "price", required = false) double price,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "insuranceType", required = false) String insuranceType,
            @RequestParam(name = "policyHolder", required = false) Long policyHolder,
            @PageableDefault Pageable pageable) {
        Page<InsurancePolicyDto> insurancePolicyDtos = insurancePolicyService.getFilteredInsurances(id, policyNumber, userId, startDate, endDate, price, status, insuranceType, policyHolder, pageable);

        if (insurancePolicyDtos.isEmpty()) {
            return new ResponseEntity<>("No insurance policies found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(insurancePolicyDtos, HttpStatus.OK);
    }
}