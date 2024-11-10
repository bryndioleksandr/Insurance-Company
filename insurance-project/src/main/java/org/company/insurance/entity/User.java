package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.Role;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class User extends Person{
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<InsurancePolicy> insurancePolicies;
}
