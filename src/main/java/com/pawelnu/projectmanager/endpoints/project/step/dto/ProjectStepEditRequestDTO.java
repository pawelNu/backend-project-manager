package com.pawelnu.projectmanager.endpoints.project.step.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class ProjectStepEditRequestDTO {
  private String name;
  private UUID projectId;
  private UUID priorityValueId;
  private UUID assignedEmployeeId;
  private Instant deadline;
}
