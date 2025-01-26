package com.pawelnu.BackendProjectManager.repository;

import com.pawelnu.BackendProjectManager.entity.CompanyEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {}
