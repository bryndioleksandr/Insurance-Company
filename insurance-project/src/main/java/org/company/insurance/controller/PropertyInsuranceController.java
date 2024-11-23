package org.company.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.PropertyInsuranceCreationDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.service.PropertyInsuranceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/property-insurances")
@AllArgsConstructor
public class PropertyInsuranceController {
    private final PropertyInsuranceService propertyInsuranceService;

    @Operation(
            summary = "Get property insurance by ID",
            description = "Fetches a property insurance by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Property insurance found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PropertyInsuranceDto.class))),
                    @ApiResponse(responseCode = "404", description = "Property insurance not found")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<PropertyInsuranceDto> getPropertyInsuranceById(@PathVariable Long id){
        return ResponseEntity.ok(propertyInsuranceService.getPropertyInsuranceById(id));
    }

    @Operation(
            summary = "Create a new property insurance",
            description = "Creates a new property insurance by providing necessary details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Property insurance created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PropertyInsuranceDto.class)))
            }
    )
    @PostMapping
    public ResponseEntity<PropertyInsuranceDto> createPropertyInsurance(@RequestBody PropertyInsuranceCreationDto propertyInsuranceDto){
        return ResponseEntity.ok(propertyInsuranceService.createPropertyInsurance(propertyInsuranceDto));
    }

    @Operation(
            summary = "Update an existing property insurance",
            description = "Updates details of an existing property insurance",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Property insurance updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PropertyInsuranceDto.class)))
            }
    )
    @PutMapping
    public ResponseEntity<PropertyInsuranceDto> updateProperty(@RequestBody PropertyInsuranceDto propertyInsuranceDto) {
        return ResponseEntity.ok(propertyInsuranceService.updatePropertyInsurance(propertyInsuranceDto));
    }

    @Operation(
            summary = "Delete property insurance by ID",
            description = "Deletes a property insurance from the system using its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Property insurance deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Property insurance not found")
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePropertyById(@PathVariable("id") Long id) {
        propertyInsuranceService.deletePropertyInsuranceById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all property insurances",
            description = "Fetches a paginated list of all property insurances",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of property insurances",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PropertyInsuranceDto.class))),
                    @ApiResponse(responseCode = "204", description = "No property insurances found")
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllProperty(Pageable pageable) {
        Page<PropertyInsuranceDto> propertyInsuranceDtos = propertyInsuranceService.getAllProperty(pageable);
        if(propertyInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(propertyInsuranceDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get sorted property insurances",
            description = "Fetches a paginated list of property insurances sorted by a specified field",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sorted list of property insurances",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PropertyInsuranceDto.class))),
                    @ApiResponse(responseCode = "204", description = "No property insurances found")
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedProperty(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<PropertyInsuranceDto> propertyInsuranceDtos = propertyInsuranceService.getSortedProperty(sortBy, order, pageable);
        if(propertyInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(propertyInsuranceDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered property insurances",
            description = "Fetches a filtered list of property insurances based on the provided query parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered list of property insurances",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PropertyInsuranceDto.class))),
                    @ApiResponse(responseCode = "404", description = "No property insurances found matching the filters")
            }
    )
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredProperty (
            @RequestParam(name = "id", required = false)  Long id,
            @RequestParam(name = "propertyAddress", required = false) String propertyAddress,
            @RequestParam(name = "houseSize", required = false) double houseSize,
            @RequestParam(name = "insuranceType", required = false) String insuranceType,
            @PageableDefault Pageable pageable
    ) {
        Page<PropertyInsuranceDto> propertyInsuranceDtos = propertyInsuranceService.getFilteredProperty(id, propertyAddress, houseSize, insuranceType, pageable);

        if (propertyInsuranceDtos.isEmpty()) {
            return new ResponseEntity<>("No propertyInsurances found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(propertyInsuranceDtos, HttpStatus.OK);
    }
}
