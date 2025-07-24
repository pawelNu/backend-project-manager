package com.pawelnu.projectmanager.endpoints.project.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class ProjectDTO {
  private UUID id;
  private String name;
  private String categoryName;
  private UUID categoryValueId;
  private String categoryValue;
  private UUID companyId;
  private String companyName;
  private UUID assignedEmployeeId;
  private String assignedEmployee;
  private String priorityName;
  private UUID priorityValueId;
  private String priorityValue;
}
