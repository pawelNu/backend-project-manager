package com.pawelnu.projectmanager.endpoints.project.step.comment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectStepCommentCreateRequestDTO {
  @NotNull
  @Size(min = 5, message = "Comment has to be at least 5 characters")
  private String comment;
  @NotNull private UUID projectStepId;
  @NotNull private UUID employeeId;
}
