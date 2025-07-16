package com.pawelnu.projectmanager.endpoints.project;

import java.util.UUID;
import lombok.Data;

@Data
public class ProjectEditRequestDTO {
  private String name;
  private UUID categoryValueId;
  private UUID companyId;
  private UUID assignedEmployeeId;
  private UUID priorityValueId;
}
