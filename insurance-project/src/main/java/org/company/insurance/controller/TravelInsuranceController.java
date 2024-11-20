package org.company.insurance.controller;

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

    @GetMapping("{id}")
    public ResponseEntity<TravelInsuranceDto> getTravelInsuranceById(@PathVariable Long id){
        return ResponseEntity.ok(travelInsuranceService.getTravelInsuranceById(id));
    }

    @PostMapping
    public ResponseEntity<TravelInsuranceDto> createTravelInsurance(@RequestBody TravelInsuranceCreationDto travelInsuranceDto){
        return ResponseEntity.ok(travelInsuranceService.createTravelInsurance(travelInsuranceDto));
    }

    @PutMapping
    public ResponseEntity<TravelInsuranceDto> updateTravel(@RequestBody TravelInsuranceDto travelInsuranceDto) {
        return ResponseEntity.ok(travelInsuranceService.updateTravelInsurance(travelInsuranceDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTravelById(@PathVariable("id") Long id) {
        travelInsuranceService.deleteTravelInsuranceById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllTravels(Pageable pageable) {
        Page<TravelInsuranceDto> travelInsuranceDtos = travelInsuranceService.getAllTravels(pageable);
        if(travelInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(travelInsuranceDtos, HttpStatus.OK);
    }

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
