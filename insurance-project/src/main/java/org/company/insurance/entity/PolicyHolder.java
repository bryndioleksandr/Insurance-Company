package org.company.insurance.entity;

import jakarta.persistence.*;
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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

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
