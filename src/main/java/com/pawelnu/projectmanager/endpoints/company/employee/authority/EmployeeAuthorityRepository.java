package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAuthorityRepository extends JpaRepository<EmployeeAuthorityEntity, UUID> {}
