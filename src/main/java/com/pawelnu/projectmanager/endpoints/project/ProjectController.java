package com.pawelnu.projectmanager.endpoints.project;

import com.pawelnu.projectmanager.dto.project.ProjectCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyService;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {

  private final CompanyService companyService;

  @Override
  public ResponseEntity<ProjectDTO> create(ProjectCreateRequestDTO body) {
    throw new NotImplementedException("not implemented");
  }

  @Override
  public ResponseEntity<List<ProjectSimpleDTO>> getList(String sort, String range, String filter) {
    throw new NotImplementedException("not implemented");
  }

  @Override
  public ResponseEntity<ProjectDTO> getById(UUID id) {
    throw new NotImplementedException("not implemented");
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
