package com.pawelnu.projectmanager.endpoints.company;

import com.github.javafaker.Company;
import com.querydsl.core.types.Predicate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CompanyRepository
    extends JpaRepository<CompanyEntity, UUID>, QuerydslPredicateExecutor<CompanyEntity> {

  Page<CompanyEntity> findAll(Predicate predicate, Pageable pageable);
}
