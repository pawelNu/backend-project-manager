package com.pawelnu.projectmanager.endpoints.project.step.comment.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class ProjectStepCommentCreateRequestDTO {
  private String comment;
  private UUID projectStepId;
  private UUID employeeId;
}
