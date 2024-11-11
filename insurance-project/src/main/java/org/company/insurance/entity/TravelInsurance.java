package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.TravelType;

import java.util.Date;

@Entity
@Table(name = "auto_insurances")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class TravelInsurance extends BaseEntity{

    @Column(name = "trip_start_date")
    private Date tripStartDate;

    @Column(name = "trip_end_date")
    private Date tripEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_type")
    private TravelType type;

    @OneToOne
    @JoinColumn(name = "policy_id")
    private InsurancePolicy insurancePolicy;

}
