package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "claim_assessments")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClaimAssessment extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "claim_id")
    private Claim claim;

    @Column(name = "assessment_date")
    private LocalDate assesmentDate;

    @Column(name = "assessment_notes", length = 1000)
    private String notes;

    @Column(name = "assessment_amount")
    private Double assessmentAmount;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;
}
