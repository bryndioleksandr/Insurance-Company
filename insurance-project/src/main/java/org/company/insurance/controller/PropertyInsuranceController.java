package org.company.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.dto.PropertyInsuranceCreationDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.exception.PropertyInsuranceNotFoundException;
import org.company.insurance.service.AgentService;
import org.company.insurance.service.PropertyInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/property-insurances")
@AllArgsConstructor
public class PropertyInsuranceController {
    private final PropertyInsuranceService propertyInsuranceService;
    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);


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
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<PropertyInsuranceDto> createPropertyInsurance(@RequestBody PropertyInsuranceCreationDto propertyInsuranceDto){
        return ResponseEntity.ok(propertyInsuranceService.createPropertyInsurance(propertyInsuranceDto));
    }

//    @Operation(
//            summary = "Update an existing property insurance",
//            description = "Updates details of an existing property insurance",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Property insurance updated",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = PropertyInsuranceDto.class)))
//            }
//    )
//    @PutMapping
//    public ResponseEntity<PropertyInsuranceDto> updateProperty(@RequestBody PropertyInsuranceDto propertyInsuranceDto) {
//        return ResponseEntity.ok(propertyInsuranceService.updatePropertyInsurance(propertyInsuranceDto));
//    }

    @Operation(
            summary = "Update an existing property insurance",
            description = "Updates the property insurance details with the provided data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Property insurance updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PropertyInsuranceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Property insurance not found")
            }
    )
    @PutMapping("/{policyId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_AGENT')")
    public ResponseEntity<PropertyInsuranceDto> updatePropertyInsuranceByPolicyId(
            @PathVariable("policyId") Long policyId,
            @RequestBody PropertyInsuranceDto propertyInsuranceDto) {
        try {
            PropertyInsuranceDto updatedPropertyInsurance = propertyInsuranceService.updatePropertyInsuranceByPolicyId(policyId, propertyInsuranceDto);
            return ResponseEntity.ok(updatedPropertyInsurance);
        } catch (PropertyInsuranceNotFoundException e) {
            logger.error("PropertyInsuranceNotFoundException: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Delete property insurance by ID",
            description = "Deletes a property insurance from the system using its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Property insurance deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Property insurance not found")
            }
    )
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredProperty (
            @RequestParam(name = "id", required = false)  Long id,
            @RequestParam(name = "propertyAddress", required = false) String propertyAddress,
            @RequestParam(name = "houseSize", required = false) Double houseSize,
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
