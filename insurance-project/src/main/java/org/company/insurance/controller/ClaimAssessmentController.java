package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.company.insurance.dto.ClaimAssessmentCreationDto;
import org.company.insurance.dto.ClaimAssessmentDto;
import org.company.insurance.dto.ClaimDto;
import org.company.insurance.dto.ClaimAssessmentDto;
import org.company.insurance.service.ClaimAssessmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PutMapping
    public ResponseEntity<ClaimAssessmentDto> updateTravel(@RequestBody ClaimAssessmentDto claimAssessmentDto) {
        return ResponseEntity.ok(claimAssessmentService.updateClaimAssessment(claimAssessmentDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTravelById(@PathVariable("id") Long id) {
        claimAssessmentService.deleteClaimAssessmentById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllClaimAssessment(Pageable pageable) {
        Page<ClaimAssessmentDto> claimAssessmentDtos = claimAssessmentService.getAllClaimAssessments(pageable);
        if(claimAssessmentDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(claimAssessmentDtos, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedClaimAssessment(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<ClaimAssessmentDto> claimAssessmentDtos = claimAssessmentService.getSortedClaimAssessments(sortBy, order, pageable);
        if(claimAssessmentDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(claimAssessmentDtos, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredClaimAssessment (
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "assessmentDate", required = false) LocalDate assessmentDate,
            @RequestParam(name = "notes", required = false) String notes,
            @RequestParam(name = "assessmentAmount", required = false) double assessmentAmount,
            @RequestParam(name = "agent", required = false) Long agent,
            @PageableDefault Pageable pageable
    ) {
        Page<ClaimAssessmentDto> claimAssessmentDtos = claimAssessmentService.getFilteredClaimAssessments(id, assessmentDate, notes, assessmentAmount, agent, pageable);

        if (claimAssessmentDtos.isEmpty()) {
            return new ResponseEntity<>("No claimAssessments found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(claimAssessmentDtos, HttpStatus.OK);
    }

}
