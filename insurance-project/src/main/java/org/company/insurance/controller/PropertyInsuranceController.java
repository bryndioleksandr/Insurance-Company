package org.company.insurance.controller;

import lombok.AllArgsConstructor;
import org.company.insurance.dto.PropertyInsuranceCreationDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.service.PropertyInsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/property-insurances")
@AllArgsConstructor
public class PropertyInsuranceController {
    private final PropertyInsuranceService propertyInsuranceService;

    @GetMapping("{id}")
    public ResponseEntity<PropertyInsuranceDto> getPropertyInsuranceById(@PathVariable Long id){
        return ResponseEntity.ok(propertyInsuranceService.getPropertyInsuranceById(id));
    }

    @PostMapping
    public ResponseEntity<PropertyInsuranceDto> createPropertyInsurance(@RequestBody PropertyInsuranceCreationDto propertyInsuranceDto){
        return ResponseEntity.ok(propertyInsuranceService.createPropertyInsurance(propertyInsuranceDto));
    }
}
