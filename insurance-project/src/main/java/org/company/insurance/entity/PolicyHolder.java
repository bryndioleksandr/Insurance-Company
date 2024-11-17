package org.company.insurance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "policy_holders")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PolicyHolder extends Person{
    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "policyHolder")
    private List<InsurancePolicy> insurancePolicies;
    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
