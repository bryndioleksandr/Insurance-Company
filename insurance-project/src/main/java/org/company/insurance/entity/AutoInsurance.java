package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.VehicleType;

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

    @OneToOne
    @JoinColumn(name = "policy_id")
    private InsurancePolicy insurancePolicy;

    @OneToOne
    @JoinColumn(name = "holder_id")
    private PolicyHolder policyHolder;


}
