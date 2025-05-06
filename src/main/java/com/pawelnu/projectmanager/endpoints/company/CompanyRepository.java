package com.pawelnu.projectmanager.endpoints.company;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CompanyRepository
    extends JpaRepository<CompanyEntity, UUID>, QuerydslPredicateExecutor<CompanyEntity> {}
