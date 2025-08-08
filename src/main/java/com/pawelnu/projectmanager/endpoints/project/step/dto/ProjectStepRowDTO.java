package com.pawelnu.projectmanager.endpoints.project.step.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectStepRowDTO {
  private UUID id;
  private String name;
  private UUID projectId;
  private String projectName;
  private UUID priorityValueId;
  private String priorityValue;
  private UUID assignedEmployeeId;
  private String employeeFirstName;
  private String employeeLastName;
  private Instant deadline;
  private Long totalElements;
  private Integer totalPages;
}
