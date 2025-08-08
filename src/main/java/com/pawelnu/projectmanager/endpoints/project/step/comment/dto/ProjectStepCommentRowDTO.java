package com.pawelnu.projectmanager.endpoints.project.step.comment.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectStepCommentRowDTO {
  private UUID id;
  private String comment;
  private Instant created;
  private UUID projectId;
  private String projectName;
  private UUID stepId;
  private String stepName;
  private UUID employeeId;
  private String employeeFirstName;
  private String employeeLastName;
  private Long totalElements;
  private Integer totalPages;
}
