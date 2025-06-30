package com.pawelnu.projectmanager.endpoints.project;

import java.util.UUID;
import lombok.Data;

@Data
public class ProjectCreateRequestDTO {
  private String name;
  private UUID categoryId;
  private UUID companyId;
  private UUID assignedEmployeeId;
  private UUID priorityId;
}
