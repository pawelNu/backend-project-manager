package com.pawelnu.projectmanager.endpoints.project.step.comment;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectStepCommentRepository extends JpaRepository<ProjectStepCommentEntity, UUID> {
  Optional<ProjectStepCommentEntity> findByIdAndIsDeletedFalse(UUID id);
}
