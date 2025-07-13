package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeAuthorityRepository extends JpaRepository<EmployeeAuthorityEntity, UUID> {

  List<EmployeeAuthorityEntity> findAllByAuthority_IdInAndEmployee_IdAndIsDeletedFalse(
      @NotNull List<UUID> authorityIds, @NotNull UUID employeeId);

  @Query(
"""
    SELECT ea.authority.id
    FROM EmployeeAuthorityEntity ea
    WHERE ea.employee.id = :employeeId
      AND ea.authority.id IN :authorityIds
      AND ea.isDeleted = false
""")
  List<UUID> findExistingAuthorityIdsForEmployee(
      @Param("employeeId") UUID employeeId, @Param("authorityIds") List<UUID> authorityIds);

  Optional<EmployeeAuthorityEntity> findByIdAndIsDeletedFalse(UUID id);
}
