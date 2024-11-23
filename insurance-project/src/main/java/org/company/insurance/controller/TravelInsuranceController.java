package org.company.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.TravelInsuranceCreationDto;
import org.company.insurance.dto.TravelInsuranceDto;
import org.company.insurance.service.TravelInsuranceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/travel-insurances")
@AllArgsConstructor
public class TravelInsuranceController {
    private final TravelInsuranceService travelInsuranceService;

    @Operation(
            summary = "Get travel insurance by ID",
            description = "Fetches a travel insurance by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Travel insurance found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TravelInsuranceDto.class))),
                    @ApiResponse(responseCode = "404", description = "Travel insurance not found")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<TravelInsuranceDto> getTravelInsuranceById(@PathVariable Long id){
        return ResponseEntity.ok(travelInsuranceService.getTravelInsuranceById(id));
    }

    @Operation(
            summary = "Create a new travel insurance",
            description = "Creates a new travel insurance by providing necessary details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Travel insurance created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TravelInsuranceDto.class)))
            }
    )
    @PostMapping
    public ResponseEntity<TravelInsuranceDto> createTravelInsurance(@RequestBody TravelInsuranceCreationDto travelInsuranceDto){
        return ResponseEntity.ok(travelInsuranceService.createTravelInsurance(travelInsuranceDto));
    }

    @Operation(
            summary = "Update an existing travel insurance",
            description = "Updates details of an existing travel insurance",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Travel insurance updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TravelInsuranceDto.class)))
            }
    )
    @PutMapping
    public ResponseEntity<TravelInsuranceDto> updateTravel(@RequestBody TravelInsuranceDto travelInsuranceDto) {
        return ResponseEntity.ok(travelInsuranceService.updateTravelInsurance(travelInsuranceDto));
    }

    @Operation(
            summary = "Delete travel insurance by ID",
            description = "Deletes a travel insurance from the system using its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Travel insurance deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Travel insurance not found")
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTravelById(@PathVariable("id") Long id) {
        travelInsuranceService.deleteTravelInsuranceById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all travel insurances",
            description = "Fetches a paginated list of all travel insurances",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of travel insurances",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TravelInsuranceDto.class))),
                    @ApiResponse(responseCode = "204", description = "No travel insurances found")
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllTravels(Pageable pageable) {
        Page<TravelInsuranceDto> travelInsuranceDtos = travelInsuranceService.getAllTravels(pageable);
        if(travelInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(travelInsuranceDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get sorted travel insurances",
            description = "Fetches a paginated list of travel insurances sorted by a specified field",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sorted list of travel insurances",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TravelInsuranceDto.class))),
                    @ApiResponse(responseCode = "204", description = "No travel insurances found")
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedTravels(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<TravelInsuranceDto> travelInsuranceDtos = travelInsuranceService.getSortedTravels(sortBy, order, pageable);
        if(travelInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(travelInsuranceDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered travel insurances",
            description = "Fetches a filtered list of travel insurances based on the provided query parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered list of travel insurances",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TravelInsuranceDto.class))),
                    @ApiResponse(responseCode = "404", description = "No travel insurances found matching the filters")
            }
    )
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredTravels (
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "travelType", required = false) String travelType,
            @RequestParam(name = "coverageArea", required = false) String coverageArea,
            @RequestParam(name = "destination", required = false) String destination,
            @PageableDefault Pageable pageable
    ) {
        Page<TravelInsuranceDto> travelInsuranceDtos = travelInsuranceService.getFilteredTravels(id, travelType, coverageArea, destination, pageable);

        if (travelInsuranceDtos.isEmpty()) {
            return new ResponseEntity<>("No travelInsurances found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(travelInsuranceDtos, HttpStatus.OK);
    }
}
