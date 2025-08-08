package com.pawelnu.projectmanager.endpoints.project.step.comment.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectStepCommentDTO {
  private UUID id;
  private String comment;
  private UUID projectId;
  private String projectName;
  private UUID stepId;
  private String stepName;
  private UUID employeeId;
  private String employeeName;
}
