package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.ClaimType;
import org.company.insurance.enums.Status;

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
    private Date submissionDate;

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

}
