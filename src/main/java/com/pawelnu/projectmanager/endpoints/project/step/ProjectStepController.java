package com.pawelnu.projectmanager.endpoints.project.step;

import com.pawelnu.projectmanager.endpoints.project.ProjectCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.ProjectDTO;
import com.pawelnu.projectmanager.endpoints.project.ProjectEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.ProjectListResponseDTO;
import com.pawelnu.projectmanager.endpoints.project.ProjectService;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepListResponseDTO;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectStepController implements ProjectStepApi {

  private final ProjectStepService projectService;

  @Override
  public ResponseEntity<ProjectStepDTO> create(ProjectStepCreateRequestDTO body) {
    return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(body));
  }

  @Override
  public ResponseEntity<List<ProjectStepDTO>> getList(String sort, String range, String filter) {
    ProjectStepListResponseDTO result = projectService.filter(sort, range, filter);
    return ResponseEntity.ok()
        .header("Content-Range", result.getContentRange())
        .body(result.getData());
  }

  @Override
  public ResponseEntity<ProjectStepDTO> getById(UUID id) {
    return ResponseEntity.ok(projectService.getById(id));
  }

  @Override
  public ResponseEntity<ProjectStepDTO> editById(UUID id, ProjectStepEditRequestDTO body) {
    return ResponseEntity.ok(projectService.editById(id, body));
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(UUID id) {
    return ResponseEntity.ok(projectService.deleteById(id));
  }
}
