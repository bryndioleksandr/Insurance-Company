package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.company.insurance.dto.AutoInsuranceCreationDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.company.insurance.dto.AutoInsuranceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.company.insurance.service.AutoInsuranceService;

@RestController
@RequestMapping("/api/auto-insurances")
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

    @PutMapping
    public ResponseEntity<AutoInsuranceDto> updateTravel(@RequestBody AutoInsuranceDto autoInsuranceDto) {
        return ResponseEntity.ok(autoInsuranceService.updateAutoInsurance(autoInsuranceDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTravelById(@PathVariable("id") Long id) {
        autoInsuranceService.deleteAutoInsuranceById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllAutoInsurances(Pageable pageable) {
        Page<AutoInsuranceDto> autoInsuranceDtos = autoInsuranceService.getAllAutoInsurances(pageable);
        if(autoInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(autoInsuranceDtos, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedAutoInsurances(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<AutoInsuranceDto> autoInsuranceDtos = autoInsuranceService.getSortedAutoInsurances(sortBy, order, pageable);
        if(autoInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(autoInsuranceDtos, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredAutoInsurances (
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "engineCapacity", required = false) double engineCapacity,
            @RequestParam(name = "brand", required = false) String brand,
            @RequestParam(name = "model", required = false) String model,
            @RequestParam(name = "year", required = false) int year,
            @RequestParam(name = "plate", required = false) String plate,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "insuranceType", required = false) String insuranceType,
            @RequestParam(name = "insurancePolicy", required = false) Long insurancePolicy,
            @PageableDefault Pageable pageable
    ) {
        Page<AutoInsuranceDto> autoInsuranceDtos = autoInsuranceService.getFilteredAutoInsurances(id, engineCapacity, brand, model, year, plate, type, insuranceType, insurancePolicy, pageable);

        if (autoInsuranceDtos.isEmpty()) {
            return new ResponseEntity<>("No autoInsurances found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(autoInsuranceDtos, HttpStatus.OK);
    }
}
