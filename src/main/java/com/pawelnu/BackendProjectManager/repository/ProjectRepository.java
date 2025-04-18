package com.pawelnu.BackendProjectManager.repository;

import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRepository
    extends JpaRepository<ProjectEntity, UUID>, JpaSpecificationExecutor<ProjectEntity> {}
