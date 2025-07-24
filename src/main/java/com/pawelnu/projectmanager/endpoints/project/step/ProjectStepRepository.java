package com.pawelnu.projectmanager.endpoints.project.step;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectStepRepository extends JpaRepository<ProjectStepEntity, UUID> {}
