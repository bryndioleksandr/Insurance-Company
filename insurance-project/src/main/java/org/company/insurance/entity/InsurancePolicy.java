package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.InsuranceType;

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
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "price")
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "policy_type")
    private InsuranceType insuranceType;

    @OneToMany(mappedBy = "insurancePolicy")
    private List<Claim> claims;

    @ManyToOne
    @JoinColumn(name = "holder_id")
    private PolicyHolder policyHolder;

    @OneToOne(mappedBy = "insurancePolicy", cascade = CascadeType.ALL)
    private Insurance insurance;
}
