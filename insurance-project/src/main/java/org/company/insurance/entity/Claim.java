package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.ClaimType;
import org.company.insurance.enums.Status;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "claims")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Claim extends BaseEntity{
    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private InsurancePolicy insurancePolicy;

    @Column(name = "incident_description", length = 1000)
    private String incidentDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type")
    private ClaimType claimType;

    @OneToOne(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true)
    private ClaimAssessment claimAssessment;

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public InsurancePolicy getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

    public ClaimAssessment getClaimAssessment() {
        return claimAssessment;
    }

    public void setClaimAssessment(ClaimAssessment claimAssessment) {
        this.claimAssessment = claimAssessment;
    }
}
