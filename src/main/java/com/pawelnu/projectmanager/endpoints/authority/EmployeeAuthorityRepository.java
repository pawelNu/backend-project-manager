package com.pawelnu.projectmanager.endpoints.authority;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAuthorityRepository extends JpaRepository<EmployeeAuthorityEntity, UUID> {}
