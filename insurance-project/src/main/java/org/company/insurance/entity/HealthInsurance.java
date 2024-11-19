package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.HealthInsuranceType;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_type")
    private HealthInsuranceType insuranceType;

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public InsurancePolicy getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    public HealthInsuranceType getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(HealthInsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }
}
