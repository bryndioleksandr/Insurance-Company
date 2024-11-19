package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.company.insurance.dto.AutoInsuranceCreationDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.company.insurance.service.AutoInsuranceService;

@RestController
@RequestMapping("/api/auto-insurance")
@AllArgsConstructor
public class AutoInsuranceController {
    private final AutoInsuranceService autoInsuranceService;

    @GetMapping("{id}")
    public ResponseEntity<AutoInsuranceDto> getAutoInsuranceById(@PathVariable("id") Long id){
        return ResponseEntity.ok(autoInsuranceService.getAutoInsuranceById(id));
    }

    @PostMapping
    public ResponseEntity<AutoInsuranceDto> createAutoInsurance(@RequestBody AutoInsuranceCreationDto autoInsuranceDto){
        return ResponseEntity.ok(autoInsuranceService.createAutoInsurance(autoInsuranceDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAutoInsuranceById(@PathVariable("id") Long id){
        autoInsuranceService.deleteAutoInsuranceById(id);
        return ResponseEntity.noContent().build();
    }

}
