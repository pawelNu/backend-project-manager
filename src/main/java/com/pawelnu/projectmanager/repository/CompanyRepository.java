package com.pawelnu.projectmanager.repository;

import com.pawelnu.projectmanager.entity.CompanyEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {}
