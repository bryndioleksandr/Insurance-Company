package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "health_insurances")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class HealthInsurance extends BaseEntity{

    @Column(name = "medical_history")
    private String medicalHistory;

    @OneToOne
    @JoinColumn(name = "policy_id")
    private InsurancePolicy insurancePolicy;

}
