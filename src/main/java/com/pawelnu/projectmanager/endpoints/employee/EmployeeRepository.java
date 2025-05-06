package com.pawelnu.projectmanager.endpoints.employee;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EmployeeRepository
    extends JpaRepository<EmployeeEntity, UUID>, QuerydslPredicateExecutor<EmployeeEntity> {

  Optional<EmployeeEntity> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
