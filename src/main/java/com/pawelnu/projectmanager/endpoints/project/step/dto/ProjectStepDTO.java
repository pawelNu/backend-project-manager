package com.pawelnu.projectmanager.endpoints.project.step.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class ProjectStepDTO {
  private UUID id;
  private String name;
  private UUID projectId;
  private String projectName;
  private UUID priorityValueId;
  private String priorityValue;
  private UUID assignedEmployeeId;
  private String assignedEmployee;
  private Instant deadline;
}
