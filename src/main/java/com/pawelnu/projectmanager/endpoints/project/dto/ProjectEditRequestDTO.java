package com.pawelnu.projectmanager.endpoints.project.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectEditRequestDTO {
  private String name;
  private UUID categoryValueId;
  private UUID companyId;
  private UUID assignedEmployeeId;
  private UUID priorityValueId;
}
