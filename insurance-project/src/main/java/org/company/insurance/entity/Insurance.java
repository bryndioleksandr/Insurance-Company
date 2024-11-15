package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Insurance extends BaseEntity {

    @Column(name = "coverage_amount")
    private double coverageAmount;

    @Column(name = "insurance_longevity")
    private int insuranceLongevity;


}
