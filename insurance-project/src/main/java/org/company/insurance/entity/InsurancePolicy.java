package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.InsuranceType;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "insurance_policies")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePolicy extends BaseEntity{
    @Column(name = "policy_number")
    private String policyNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "price")
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InsuranceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "policy_type")
    private InsuranceType insuranceType;

    @OneToMany(mappedBy = "insurancePolicy")
    private List<Claim> claims;

    @ManyToOne
    @JoinColumn(name = "holder_id")
    private PolicyHolder policyHolder;

//    @OneToOne(mappedBy = "insurancePolicy", optional = true)
//    private AutoInsurance autoInsurance;
//
//    @OneToOne(mappedBy = "insurancePolicy", optional = true)
//    private TravelInsurance travelInsurance;
//
//    @OneToOne(mappedBy = "insurancePolicy", optional = true)
//    private HealthInsurance healthInsurance;
//
//    @OneToOne(mappedBy = "insurancePolicy", optional = true)
//    private PropertyInsurance propertyInsurance;

    public InsuranceStatus getStatus() {
        return status;
    }

    public void setStatus(InsuranceStatus status) {
        this.status = status;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }
}
