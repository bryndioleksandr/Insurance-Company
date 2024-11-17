package org.company.insurance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "agents")
@Getter
@Setter
@ToString

public class Agent extends Person{
    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "position")
    private String position;

    @OneToMany(mappedBy = "agent")
    private List<ClaimAssessment> claimAssessments;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
}
