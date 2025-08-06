package com.pawelnu.projectmanager.endpoints.project.step.dto;

import com.pawelnu.projectmanager.entity.RowPagination;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ProjectStepRowDTO extends RowPagination {
  private UUID id;
  private String name;
  private UUID projectId;
  private String projectName;
  private UUID priorityId;
  private String priorityValue;
  private UUID assignedEmployeeId;
  private String employeeFirstName;
  private String employeeLastName;
  private Instant deadline;
}
