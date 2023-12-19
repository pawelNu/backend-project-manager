package com.pawelnu.BackendProjectManager.repository.project;

import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository
        extends JpaRepository<ProjectEntity, UUID>, JpaSpecificationExecutor<ProjectEntity> {}
