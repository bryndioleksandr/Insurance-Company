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
    private Double coverageAmount;

    @Column(name = "insurance_longevity")
    private int insuranceLongevity;

    public Double getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(Double coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public int getInsuranceLongevity() {
        return insuranceLongevity;
    }

    public void setInsuranceLongevity(int insuranceLongevity) {
        this.insuranceLongevity = insuranceLongevity;
    }
}
