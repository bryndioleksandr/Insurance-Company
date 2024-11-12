package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.*;

@Entity
@Table(name = "auto_insurances")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AutoInsurance extends BaseEntity{

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "year")
    private int year;

    @Column(name = "plate")
    private String plate;

    @Column(name = "price")
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VehicleType type;

    @Column(name = "insurance_longevity")
    private int insuranceLongevity;

    @Column(name = "coverage_amount")
    private double coverageAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_type")
    private AutoInsuranceType insuranceType;

    @OneToOne
    @JoinColumn(name = "policy_id")
    private InsurancePolicy insurancePolicy;

    @OneToOne
    @JoinColumn(name = "holder_id")
    private PolicyHolder policyHolder;
}
