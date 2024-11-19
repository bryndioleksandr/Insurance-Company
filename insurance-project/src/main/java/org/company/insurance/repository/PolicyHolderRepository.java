package org.company.insurance.repository;

import org.company.insurance.entity.PolicyHolder;
import org.company.insurance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyHolderRepository extends JpaRepository<PolicyHolder, Long> {
    Optional<PolicyHolder> findByUser(User user);
}