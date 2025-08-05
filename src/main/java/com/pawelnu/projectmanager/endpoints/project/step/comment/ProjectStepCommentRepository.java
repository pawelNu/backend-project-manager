package com.pawelnu.projectmanager.endpoints.project.step.comment;

import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectStepCommentRepository extends JpaRepository<ProjectStepEntity, UUID> {
  Optional<ProjectStepCommentEntity> findByIdAndIsDeletedFalse(UUID id);
}
