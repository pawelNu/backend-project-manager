package com.pawelnu.projectmanager.endpoints.project;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectCreateRequestDTO {
  private String name;
  private UUID categoryId;
  private UUID companyId;
  private UUID assignedEmployeeId;
  private UUID priorityId;
}
