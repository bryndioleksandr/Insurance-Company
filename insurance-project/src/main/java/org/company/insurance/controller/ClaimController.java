package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.ClaimCreationDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.service.ClaimService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/claims")
@AllArgsConstructor
public class ClaimController {
    private final ClaimService claimService;

    @GetMapping("{id}")
    public ResponseEntity<ClaimDto> getClaimById(@PathVariable Long id){
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    @PostMapping
    public ResponseEntity<ClaimDto> createClaim(@RequestBody ClaimCreationDto claimCreationDto){
        return ResponseEntity.ok(claimService.createClaim(claimCreationDto));
    }

    @PutMapping
    public ResponseEntity<ClaimDto> updateClaim(@RequestBody ClaimDto claimDto) {
        return ResponseEntity.ok(claimService.updateClaim(claimDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClaimById(@PathVariable("id") Long id) {
        claimService.deleteClaimById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllClaims(Pageable pageable) {
        Page<ClaimDto> claimDtos = claimService.getAllClaims(pageable);
        if(claimDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(claimDtos, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedClaims(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<ClaimDto> claimDtos = claimService.getSortedClaims(sortBy, order, pageable);
        if(claimDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(claimDtos, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredClaims (
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "submissionDate", required = false) LocalDate submissionDate,
            @RequestParam(name = "amount", required = false) double amount,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "insurancePolicy", required = false) Long insurancePolicy,
            @RequestParam(name = "incidentDescription", required = false) String incidentDescription,
            @RequestParam(name = "claimType", required = false) String claimType,
            @PageableDefault Pageable pageable
    ) {
        Page<ClaimDto> claimDtos = claimService.getFilteredClaims(id, submissionDate, amount, status, insurancePolicy, incidentDescription, claimType, pageable);

        if (claimDtos.isEmpty()) {
            return new ResponseEntity<>("No claims found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(claimDtos, HttpStatus.OK);
    }
}
