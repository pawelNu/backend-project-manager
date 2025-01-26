package com.pawelnu.BackendProjectManager.config.init;

import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.enums.ProjectStatus;
import com.pawelnu.BackendProjectManager.repository.project.ProjectRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectInit {
  private final ProjectRepository projectRepository;

  @PostConstruct
  private void loadData() {

    List<ProjectEntity> projects =
        List.of(
            ProjectEntity.builder()
                .name("Transport Company")
                .projectStatus(ProjectStatus.MAINTAINED)
                .build(),
            ProjectEntity.builder()
                .name("Telecommunication Company")
                .projectStatus(ProjectStatus.MAINTAINED)
                .build(),
            ProjectEntity.builder()
                .name("Outsourcing Company")
                .projectStatus(ProjectStatus.CLOSED)
                .build());

    projectRepository.saveAll(projects);
  }
}
