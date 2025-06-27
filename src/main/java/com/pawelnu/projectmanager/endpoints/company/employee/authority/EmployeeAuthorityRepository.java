package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAuthorityRepository extends JpaRepository<EmployeeAuthorityEntity, UUID> {

  List<EmployeeAuthorityEntity> findAllByAuthority_IdInAndEmployee_IdAndIsDeletedFalse(
      @NotNull List<UUID> authorityIds, @NotNull UUID employeeId);
}
