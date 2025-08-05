package com.pawelnu.projectmanager.endpoints.project.step;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectStepRepository extends JpaRepository<ProjectStepEntity, UUID> {
  Optional<ProjectStepEntity> findByIdAndIsDeletedFalse(UUID id);
}
