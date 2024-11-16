package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.TravelType;

@Entity
@Table(name = "travel_insurances")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TravelInsurance extends Insurance {

    @Column(name = "destination")
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_type")
    private TravelType travelType;

    @OneToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private InsurancePolicy insurancePolicy;
}
