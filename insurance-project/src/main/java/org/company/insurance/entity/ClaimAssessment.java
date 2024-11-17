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
    private LocalDate assessmentDate;

    @Column(name = "assessment_notes", length = 1000)
    private String notes;

    @Column(name = "assessment_amount")
    private Double assessmentAmount;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getAssessmentAmount() {
        return assessmentAmount;
    }

    public void setAssessmentAmount(Double assessmentAmount) {
        this.assessmentAmount = assessmentAmount;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
