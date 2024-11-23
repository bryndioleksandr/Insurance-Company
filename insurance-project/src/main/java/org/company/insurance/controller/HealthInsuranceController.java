package org.company.insurance.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.HealthInsuranceCreationDto;
import org.company.insurance.dto.HealthInsuranceDto;
import org.company.insurance.dto.HealthInsuranceDto;
import org.company.insurance.service.HealthInsuranceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-insurances")
@AllArgsConstructor
public class HealthInsuranceController {
    private final HealthInsuranceService healthInsuranceService;

    @Operation(
            summary = "Get health insurance by ID",
            description = "Fetches the health insurance with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Health insurance found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HealthInsuranceDto.class))),
                    @ApiResponse(responseCode = "404", description = "Health insurance not found")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<HealthInsuranceDto> getHealthInsuranceById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(healthInsuranceService.getHealthInsuranceById(id));
    }

    @Operation(
            summary = "Create a new health insurance",
            description = "Creates a new health insurance with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Health insurance created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HealthInsuranceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    public ResponseEntity<HealthInsuranceDto> createHealthInsurance(@RequestBody HealthInsuranceCreationDto healthInsuranceDto) {
        return ResponseEntity.ok(healthInsuranceService.createHealthInsurance(healthInsuranceDto));
    }

    @Operation(
            summary = "Delete health insurance by ID",
            description = "Deletes the health insurance with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Health insurance deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Health insurance not found")
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteHealthInsuranceById(@PathVariable("id") Long id) {
        healthInsuranceService.deleteHealthInsuranceById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update health insurance",
            description = "Updates an existing health insurance with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Health insurance updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HealthInsuranceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PutMapping
    public ResponseEntity<HealthInsuranceDto> updateHealthInsurance(@RequestBody HealthInsuranceDto healthInsuranceDto) {
        return ResponseEntity.ok(healthInsuranceService.updateHealthInsurance(healthInsuranceDto));
    }

    @Operation(
            summary = "Get all health insurances",
            description = "Fetches a paginated list of all health insurances",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of health insurances",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HealthInsuranceDto.class))),
                    @ApiResponse(responseCode = "204", description = "No health insurances found")
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllHealth(Pageable pageable) {
        Page<HealthInsuranceDto> healthInsuranceDtos = healthInsuranceService.getAllHealth(pageable);
        if (healthInsuranceDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(healthInsuranceDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get sorted health insurances",
            description = "Fetches a sorted list of health insurances based on provided parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sorted list of health insurances",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HealthInsuranceDto.class))),
                    @ApiResponse(responseCode = "204", description = "No health insurances found")
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedHealth(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<HealthInsuranceDto> healthInsuranceDtos = healthInsuranceService.getSortedHealth(sortBy, order, pageable);
        if (healthInsuranceDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(healthInsuranceDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered health insurances",
            description = "Fetches a filtered list of health insurances based on the provided query parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered list of health insurances",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HealthInsuranceDto.class))),
                    @ApiResponse(responseCode = "404", description = "No health insurances found matching the filters")
            }
    )
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredHealth(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "medicalHistory", required = false) String medicalHistory,
            @RequestParam(name = "policyId", required = false) Long policyId,
            @RequestParam(name = "insuranceType", required = false) String insuranceType,
            @PageableDefault Pageable pageable) {
        Page<HealthInsuranceDto> healthInsuranceDtos = healthInsuranceService.getFilteredHealth(id, medicalHistory, policyId, insuranceType, pageable);

        if (healthInsuranceDtos.isEmpty()) {
            return new ResponseEntity<>("No health insurances found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(healthInsuranceDtos, HttpStatus.OK);
    }
}