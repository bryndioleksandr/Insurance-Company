package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "insurances")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Insurance extends BaseEntity {

    @Column(name = "coverage_amount")
    private double coverageAmount;

    @Column(name = "insurance_longevity")
    private int insuranceLongevity;

    @OneToOne
    @JoinColumn(name = "policy_id")
    private InsurancePolicy insurancePolicy;
}
