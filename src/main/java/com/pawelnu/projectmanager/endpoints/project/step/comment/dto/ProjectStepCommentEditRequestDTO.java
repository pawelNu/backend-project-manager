package com.pawelnu.projectmanager.endpoints.project.step.comment.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectStepCommentEditRequestDTO {
  private String comment;
  private UUID stepId;
  private UUID employeeId;
}
