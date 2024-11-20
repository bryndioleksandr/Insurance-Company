package org.company.insurance.controller;


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

    @PutMapping
    public ResponseEntity<HealthInsuranceDto> updateTravel(@RequestBody HealthInsuranceDto healthInsuranceDto) {
        return ResponseEntity.ok(healthInsuranceService.updateHealthInsurance(healthInsuranceDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTravelById(@PathVariable("id") Long id) {
        healthInsuranceService.deleteHealthInsuranceById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllHealth(Pageable pageable) {
        Page<HealthInsuranceDto> healthInsuranceDtos = healthInsuranceService.getAllHealth(pageable);
        if(healthInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(healthInsuranceDtos, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedHealth(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<HealthInsuranceDto> healthInsuranceDtos = healthInsuranceService.getSortedHealth(sortBy, order, pageable);
        if(healthInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(healthInsuranceDtos, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredHealth (
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "medicalHistory", required = false) String medicalHistory,
            @RequestParam(name = "policyId", required = false) Long policyId,
            @RequestParam(name = "insuranceType", required = false) String insuranceType,
            @PageableDefault Pageable pageable
    ) {
        Page<HealthInsuranceDto> healthInsuranceDtos = healthInsuranceService.getFilteredHealth(id, medicalHistory, policyId, insuranceType, pageable);

        if (healthInsuranceDtos.isEmpty()) {
            return new ResponseEntity<>("No healthInsurances found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(healthInsuranceDtos, HttpStatus.OK);
    }
}
