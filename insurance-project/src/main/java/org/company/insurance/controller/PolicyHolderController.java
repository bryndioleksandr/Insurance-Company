package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.PolicyHolderCreationDto;
import org.company.insurance.dto.PolicyHolderDto;

import org.company.insurance.service.PolicyHolderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

    @PutMapping
    public ResponseEntity<PolicyHolderDto> updatePolicyHolder(@RequestBody PolicyHolderDto policyHolderDto) {
        return ResponseEntity.ok(policyHolderService.updatePolicyHolder(policyHolderDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePolicyHolderById(@PathVariable("id") Long id) {
        policyHolderService.deletePolicyHolderById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllHolders(Pageable pageable) {
        Page<PolicyHolderDto> policyHolderDtos = policyHolderService.getAllHolders(pageable);
        if(policyHolderDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(policyHolderDtos, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedHolders(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<PolicyHolderDto> policyHolderDtos = policyHolderService.getSortedHolders(sortBy, order, pageable);
        if(policyHolderDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(policyHolderDtos, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredHolders (
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "passportNumber", required = false) String passportNumber,
            @RequestParam(name = "address", required = false) String address,
            @PageableDefault Pageable pageable
    ) {
        Page<PolicyHolderDto> policyHolderDtos = policyHolderService.getFilteredHolders(id, passportNumber, address, pageable);

        if (policyHolderDtos.isEmpty()) {
            return new ResponseEntity<>("No policyHolders found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(policyHolderDtos, HttpStatus.OK);
    }
}
