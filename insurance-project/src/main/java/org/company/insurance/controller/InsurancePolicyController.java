package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.InsurancePolicyCreationDto;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.entity.Insurance;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.service.InsurancePolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insurance-policies")
@AllArgsConstructor
public class InsurancePolicyController {
    private final InsurancePolicyService insurancePolicyService;

    @GetMapping("{id}")
    public ResponseEntity<InsurancePolicyDto> getInsurancePolicyById(@PathVariable Long id){
        return ResponseEntity.ok(insurancePolicyService.getInsurancePolicyById(id));
    }

    @PostMapping
    public ResponseEntity<InsurancePolicyDto> createInsurancePolicy(@RequestBody InsurancePolicyCreationDto insurancePolicy){
        return ResponseEntity.ok(insurancePolicyService.createInsurancePolicy(insurancePolicy));
    }

}
