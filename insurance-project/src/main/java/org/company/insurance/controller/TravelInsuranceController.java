package org.company.insurance.controller;

import lombok.AllArgsConstructor;
import org.company.insurance.dto.TravelInsuranceCreationDto;
import org.company.insurance.dto.TravelInsuranceDto;
import org.company.insurance.service.TravelInsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
