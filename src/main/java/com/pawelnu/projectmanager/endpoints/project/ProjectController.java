package com.pawelnu.projectmanager.endpoints.project;

import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {

  private final ProjectService projectService;

  @Override
  public ResponseEntity<ProjectDTO> create(ProjectCreateRequestDTO body) {
    return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(body));
  }

  @Override
  public ResponseEntity<List<ProjectSimpleDTO>> getList(String sort, String range, String filter) {
    throw new NotImplementedException("not implemented");
  }

  @Override
  public ResponseEntity<ProjectDTO> getById(UUID id) {
    return ResponseEntity.ok(projectService.getById(id));
  }

  @Override
  public ResponseEntity<ProjectDTO> editById(UUID id, ProjectEditRequestDTO body) {
    throw new NotImplementedException("not implemented");
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(UUID id) {
    throw new NotImplementedException("not implemented");
  }
}
