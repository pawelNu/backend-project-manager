package com.pawelnu.projectmanager.endpoints.authority;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityEmployeeRepository extends JpaRepository<EmployeeAuthorityEntity, UUID> {}
