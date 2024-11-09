package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.Role;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class User extends BaseEntity{
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<InsurancePolicy> insurancePolicies;
}
