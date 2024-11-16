package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "health_insurances")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class HealthInsurance extends Insurance {

    @Column(name = "medical_history")
    private String medicalHistory;

    @OneToOne
    @JoinColumn(name = "policy_id")
    private InsurancePolicy insurancePolicy;
}
