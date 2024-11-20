package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.HealthInsuranceCreationDto;
import org.company.insurance.dto.HealthInsuranceDto;
import org.company.insurance.service.HealthInsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-insurances")
@AllArgsConstructor
public class HealthInsuranceController {
    private final HealthInsuranceService healthInsuranceService;

    @GetMapping("{id}")
    public ResponseEntity<HealthInsuranceDto> getHealthInsuranceById(@PathVariable Long id){
        return ResponseEntity.ok(healthInsuranceService.getHealthInsuranceById(id));
    }

    @PostMapping
    public ResponseEntity<HealthInsuranceDto> createHealthInsurance(@RequestBody HealthInsuranceCreationDto healthInsuranceDto){
        return ResponseEntity.ok(healthInsuranceService.createHealthInsurance(healthInsuranceDto));
    }

//    @DeleteMapping("{id}")
//    public ResponseEntity<Void> deleteHealthInsuranceById(@PathVariable Long id){
//        healthInsuranceService.deleteHealthInsuranceById(id);
//        return ResponseEntity.noContent().build();
//    }
}
