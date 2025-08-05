package com.pawelnu.projectmanager.endpoints.project.step.comment.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectStepCommentListResponseDTO {
  private List<ProjectStepCommentDTO> data;
  private String contentRange;
}
