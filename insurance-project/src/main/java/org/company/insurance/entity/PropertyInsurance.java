package org.company.insurance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "property_insurances")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PropertyInsurance extends BaseEntity{

    @Column(name = "property_address")
    private String propertyAddress;

    @Column(name = "insurance_longevity")
    private int insuranceLongevity;

    @Column(name = "coverage_amount")
    private double coverageAmount;
}
