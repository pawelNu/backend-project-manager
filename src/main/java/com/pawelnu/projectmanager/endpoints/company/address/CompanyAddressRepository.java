package com.pawelnu.projectmanager.endpoints.company.address;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CompanyAddressRepository
    extends JpaRepository<CompanyAddressEntity, UUID>,
        QuerydslPredicateExecutor<CompanyAddressEntity> {}
