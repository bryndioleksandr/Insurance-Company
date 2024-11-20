package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.InsurancePolicyCreationDto;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.dto.InsurancePolicyDto;
import org.company.insurance.entity.Insurance;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.service.InsurancePolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PutMapping
    public ResponseEntity<InsurancePolicyDto> updateInsurancePolicy(@RequestBody InsurancePolicyDto insurancePolicyDto) {
        return ResponseEntity.ok(insurancePolicyService.updateInsurancePolicy(insurancePolicyDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteInsurancePolicyById(@PathVariable Long id){
        insurancePolicyService.deleteInsurancePolicyById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllInsurances(Pageable pageable) {
        Page<InsurancePolicyDto> insurancePolicyDtos = insurancePolicyService.getAllInsurances(pageable);
        if(insurancePolicyDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(insurancePolicyDtos, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedInsurances(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<InsurancePolicyDto> insurancePolicyDtos = insurancePolicyService.getSortedInsurances(sortBy, order, pageable);
        if(insurancePolicyDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(insurancePolicyDtos, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredInsurances (
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "policyNumber", required = false) String policyNumber,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "price", required = false) double price,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "insuranceType", required = false) String insuranceType,
            @RequestParam(name = "policyHolder", required = false) Long policyHolder,
            @PageableDefault Pageable pageable
            ) {
        Page<InsurancePolicyDto> insurancePolicyDtos = insurancePolicyService.getFilteredInsurances(id, policyNumber, userId, startDate, endDate, price, status, insuranceType, policyHolder, pageable);

        if (insurancePolicyDtos.isEmpty()) {
            return new ResponseEntity<>("No insurancePolicies found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(insurancePolicyDtos, HttpStatus.OK);
    }

}
