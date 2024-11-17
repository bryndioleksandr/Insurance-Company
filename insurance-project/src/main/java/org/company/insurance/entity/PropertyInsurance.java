package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "property_insurances")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PropertyInsurance extends Insurance {

    @Column(name = "property_address")
    private String propertyAddress;

    @OneToOne
    @JoinColumn(name = "policy_id")
    private InsurancePolicy insurancePolicy;

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public InsurancePolicy getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }
}
