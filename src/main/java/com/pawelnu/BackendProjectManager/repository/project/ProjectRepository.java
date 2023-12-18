package com.pawelnu.BackendProjectManager.repository.project;

import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID>,
        JpaSpecificationExecutor<ProjectEntity> {
}
