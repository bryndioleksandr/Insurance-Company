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

    public double getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(double coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public int getInsuranceLongevity() {
        return insuranceLongevity;
    }

    public void setInsuranceLongevity(int insuranceLongevity) {
        this.insuranceLongevity = insuranceLongevity;
    }
}
