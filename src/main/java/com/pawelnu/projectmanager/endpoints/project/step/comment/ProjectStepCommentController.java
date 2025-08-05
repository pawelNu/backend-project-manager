package com.pawelnu.projectmanager.endpoints.project.step.comment;

import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentDTO;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentListResponseDTO;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectStepCommentController implements ProjectStepCommentApi {

  private final ProjectStepCommentService projectStepCommentService;

  @Override
  public ResponseEntity<ProjectStepCommentDTO> create(ProjectStepCommentCreateRequestDTO body) {
    return ResponseEntity.status(HttpStatus.CREATED).body(projectStepCommentService.create(body));
  }

  @Override
  public ResponseEntity<List<ProjectStepCommentDTO>> getList(
      String sort, String range, String filter) {
    ProjectStepCommentListResponseDTO result = projectStepCommentService.filter(sort, range, filter);
    return ResponseEntity.ok()
        .header("Content-Range", result.getContentRange())
        .body(result.getData());
  }

  @Override
  public ResponseEntity<ProjectStepCommentDTO> getById(UUID id) {
    return ResponseEntity.ok(projectStepCommentService.getById(id));
  }

  @Override
  public ResponseEntity<ProjectStepCommentDTO> editById(
      UUID id, ProjectStepCommentEditRequestDTO body) {
    return ResponseEntity.ok(projectStepCommentService.editById(id, body));
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(UUID id) {
    return ResponseEntity.ok(projectStepCommentService.deleteById(id));
  }
}
