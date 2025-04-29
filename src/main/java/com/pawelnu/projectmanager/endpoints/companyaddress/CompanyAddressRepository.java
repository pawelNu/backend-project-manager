package com.pawelnu.projectmanager.endpoints.companyaddress;

import com.querydsl.core.types.Predicate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CompanyAddressRepository
    extends JpaRepository<CompanyAddressEntity, UUID>,
        QuerydslPredicateExecutor<CompanyAddressEntity> {

  //  @EntityGraph(attributePaths = {"company"})
  Page<CompanyAddressEntity> findAll(Predicate predicate, Pageable pageable);
}
