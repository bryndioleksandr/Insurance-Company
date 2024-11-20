package org.company.insurance.controller;

import lombok.AllArgsConstructor;
import org.company.insurance.dto.PropertyInsuranceCreationDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.dto.PropertyInsuranceDto;
import org.company.insurance.service.PropertyInsuranceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/property-insurances")
@AllArgsConstructor
public class PropertyInsuranceController {
    private final PropertyInsuranceService propertyInsuranceService;

    @GetMapping("{id}")
    public ResponseEntity<PropertyInsuranceDto> getPropertyInsuranceById(@PathVariable Long id){
        return ResponseEntity.ok(propertyInsuranceService.getPropertyInsuranceById(id));
    }

    @PostMapping
    public ResponseEntity<PropertyInsuranceDto> createPropertyInsurance(@RequestBody PropertyInsuranceCreationDto propertyInsuranceDto){
        return ResponseEntity.ok(propertyInsuranceService.createPropertyInsurance(propertyInsuranceDto));
    }

    @PutMapping
    public ResponseEntity<PropertyInsuranceDto> updateProperty(@RequestBody PropertyInsuranceDto propertyInsuranceDto) {
        return ResponseEntity.ok(propertyInsuranceService.updatePropertyInsurance(propertyInsuranceDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePropertyById(@PathVariable("id") Long id) {
        propertyInsuranceService.deletePropertyInsuranceById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllProperty(Pageable pageable) {
        Page<PropertyInsuranceDto> propertyInsuranceDtos = propertyInsuranceService.getAllProperty(pageable);
        if(propertyInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(propertyInsuranceDtos, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedProperty(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<PropertyInsuranceDto> propertyInsuranceDtos = propertyInsuranceService.getSortedProperty(sortBy, order, pageable);
        if(propertyInsuranceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(propertyInsuranceDtos, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredProperty (
            @RequestParam(name = "id", required = false)  Long id,
            @RequestParam(name = "propertyAddress", required = false) String propertyAddress,
            @RequestParam(name = "houseSize", required = false) double houseSize,
            @RequestParam(name = "insuranceType", required = false) String insuranceType,
            @PageableDefault Pageable pageable
    ) {
        Page<PropertyInsuranceDto> propertyInsuranceDtos = propertyInsuranceService.getFilteredProperty(id, propertyAddress, houseSize, insuranceType, pageable);

        if (propertyInsuranceDtos.isEmpty()) {
            return new ResponseEntity<>("No propertyInsurances found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(propertyInsuranceDtos, HttpStatus.OK);
    }
}
