package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "property_insurances")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PropertyInsurance extends Insurance {

    @Column(name = "property_address")
    private String propertyAddress;
}
