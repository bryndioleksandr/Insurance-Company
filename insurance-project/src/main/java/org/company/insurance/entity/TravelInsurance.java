package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.CoverageArea;
import org.company.insurance.enums.TravelType;

@Entity
@Table(name = "travel_insurances")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TravelInsurance extends Insurance {

    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_area")
    private CoverageArea coverageArea;

    @Column(name = "destination")
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_type")
    private TravelType travelType;

    @OneToOne
    @JoinColumn(name = "policy_id")
    private InsurancePolicy insurancePolicy;

    public CoverageArea getCoverageArea() {
        return coverageArea;
    }

    public void setCoverageArea(CoverageArea coverageArea) {
        this.coverageArea = coverageArea;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public TravelType getTravelType() {
        return travelType;
    }

    public void setTravelType(TravelType travelType) {
        this.travelType = travelType;
    }

    public InsurancePolicy getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }
}
