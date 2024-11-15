package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.ClaimCreationDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.service.ClaimService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
