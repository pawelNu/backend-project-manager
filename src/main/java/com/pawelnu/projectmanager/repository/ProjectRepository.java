package com.pawelnu.projectmanager.repository;

import com.pawelnu.projectmanager.entity.ProjectEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRepository
    extends JpaRepository<ProjectEntity, UUID>, JpaSpecificationExecutor<ProjectEntity> {}
