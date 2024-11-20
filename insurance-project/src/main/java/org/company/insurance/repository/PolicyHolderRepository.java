package org.company.insurance.repository;

import org.company.insurance.entity.Agent;
import org.company.insurance.entity.PolicyHolder;
import org.company.insurance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PolicyHolderRepository extends JpaRepository<PolicyHolder, Long>, JpaSpecificationExecutor<PolicyHolder> {
    Optional<PolicyHolder> findByUserId(User user);
}