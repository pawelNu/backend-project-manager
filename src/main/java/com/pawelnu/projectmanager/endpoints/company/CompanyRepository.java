package com.pawelnu.projectmanager.endpoints.company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CompanyRepository
    extends JpaRepository<CompanyEntity, UUID>, QuerydslPredicateExecutor<CompanyEntity> {

  @Query("SELECT c.id FROM CompanyEntity c")
  List<UUID> findAllIds();

  Optional<CompanyEntity> findByIdAndIsDeletedFalse(UUID is);
}
