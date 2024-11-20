package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.company.insurance.dto.ClaimAssessmentCreationDto;
import org.company.insurance.dto.ClaimAssessmentDto;
import org.company.insurance.service.ClaimAssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claim-assessments")
@AllArgsConstructor
public class ClaimAssessmentController {
    private final ClaimAssessmentService claimAssessmentService;

    @GetMapping("{id}")
    public ResponseEntity<ClaimAssessmentDto> getClaimAssessmentById(@PathVariable Long id){
        return ResponseEntity.ok(claimAssessmentService.getClaimAssessmentById(id));
    }

    @PostMapping
    public ResponseEntity<ClaimAssessmentDto> createClaimAssessment(@RequestBody ClaimAssessmentCreationDto claimAssessmentDto){
        return ResponseEntity.ok(claimAssessmentService.createClaimAssessment(claimAssessmentDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClaimAssessment(@PathVariable Long id){
        claimAssessmentService.deleteClaimAssessmentById(id);
        return ResponseEntity.noContent().build();
    }

}
