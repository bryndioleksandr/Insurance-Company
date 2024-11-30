package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.PropertyInsuranceType;

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

    @Column(name = "house_size")
    private Double houseSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_type")
    private PropertyInsuranceType insuranceType;

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
