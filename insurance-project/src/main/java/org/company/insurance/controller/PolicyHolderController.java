package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.PolicyHolderCreationDto;
import org.company.insurance.dto.PolicyHolderDto;
import org.company.insurance.service.PolicyHolderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policy-holders")
@AllArgsConstructor
public class PolicyHolderController {
    private final PolicyHolderService policyHolderService;

    @GetMapping("{id}")
    public ResponseEntity<PolicyHolderDto> getPolicyHolderById(@PathVariable Long id){
        return ResponseEntity.ok(policyHolderService.getPolicyHolderById(id));
    }

    @PostMapping
    public ResponseEntity<PolicyHolderDto> createPolicyHolder(@RequestBody PolicyHolderCreationDto policyHolderDto){
        return ResponseEntity.ok(policyHolderService.createPolicyHolder(policyHolderDto));
    }
}
