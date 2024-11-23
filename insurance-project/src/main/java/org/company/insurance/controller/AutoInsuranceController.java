package org.company.insurance.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.company.insurance.dto.AutoInsuranceCreationDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.company.insurance.service.AutoInsuranceService;

@RestController
@RequestMapping("/api/auto-insurances")
@AllArgsConstructor
public class AutoInsuranceController {
    private final AutoInsuranceService autoInsuranceService;

    @Operation(
            summary = "Get auto insurance by ID",
            description = "Fetches the auto insurance details by the provided ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Auto insurance details found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AutoInsuranceDto.class))),
                    @ApiResponse(responseCode = "404", description = "Auto insurance not found")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<AutoInsuranceDto> getAutoInsuranceById(@PathVariable("id") Long id){
        return ResponseEntity.ok(autoInsuranceService.getAutoInsuranceById(id));
    }

    @Operation(
            summary = "Create a new auto insurance",
            description = "Creates a new auto insurance with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Auto insurance created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AutoInsuranceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    public ResponseEntity<AutoInsuranceDto> createAutoInsurance(@RequestBody AutoInsuranceCreationDto autoInsuranceDto){
        return ResponseEntity.ok(autoInsuranceService.createAutoInsurance(autoInsuranceDto));
    }

    @Operation(
            summary = "Update an existing auto insurance",
            description = "Updates the auto insurance details with the provided data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Auto insurance updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AutoInsuranceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Auto insurance not found")
            }
    )
    @PutMapping
    public ResponseEntity<AutoInsuranceDto> updateAutoInsurance(@RequestBody AutoInsuranceDto autoInsuranceDto) {
        return ResponseEntity.ok(autoInsuranceService.updateAutoInsurance(autoInsuranceDto));
    }

    @Operation(
            summary = "Delete auto insurance by ID",
            description = "Deletes the auto insurance with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Auto insurance deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Auto insurance not found")
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAutoInsuranceById(@PathVariable("id") Long id) {
        autoInsuranceService.deleteAutoInsuranceById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all auto insurances with pagination",
            description = "Fetches a paginated list of all auto insurances",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Auto insurances fetched successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AutoInsuranceDto.class))),
                    @ApiResponse(responseCode = "204", description = "No content available")
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllAutoInsurances(Pageable pageable) {
        Page<AutoInsuranceDto> autoInsuranceDtos = autoInsuranceService.getAllAutoInsurances(pageable);
        if(autoInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(autoInsuranceDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get sorted auto insurances",
            description = "Fetches a paginated list of sorted auto insurances based on provided sort criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Auto insurances sorted successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AutoInsuranceDto.class))),
                    @ApiResponse(responseCode = "204", description = "No content available")
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedAutoInsurances(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<AutoInsuranceDto> autoInsuranceDtos = autoInsuranceService.getSortedAutoInsurances(sortBy, order, pageable);
        if(autoInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(autoInsuranceDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered auto insurances",
            description = "Fetches a paginated list of auto insurances filtered by various criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Auto insurances filtered successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AutoInsuranceDto.class))),
                    @ApiResponse(responseCode = "404", description = "No auto insurances found")
            }
    )
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredAutoInsurances (
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "engineCapacity", required = false) double engineCapacity,
            @RequestParam(name = "brand", required = false) String brand,
            @RequestParam(name = "model", required = false) String model,
            @RequestParam(name = "year", required = false) int year,
            @RequestParam(name = "plate", required = false) String plate,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "insuranceType", required = false) String insuranceType,
            @RequestParam(name = "insurancePolicy", required = false) Long insurancePolicy,
            @PageableDefault Pageable pageable
    ) {
        Page<AutoInsuranceDto> autoInsuranceDtos = autoInsuranceService.getFilteredAutoInsurances(id, engineCapacity, brand, model, year, plate, type, insuranceType, insurancePolicy, pageable);

        if (autoInsuranceDtos.isEmpty()) {
            return new ResponseEntity<>("No autoInsurances found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(autoInsuranceDtos, HttpStatus.OK);
    }
}