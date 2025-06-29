package com.pawelnu.projectmanager.endpoints.project;

import java.util.UUID;
import lombok.Data;

@Data
public class ProjectDTO {
  private UUID id;
  private String name;
  private String categoryName;
  private String categoryValue;
  private String companyName;
  private String assignedEmployee;
  private String priorityName;
  private String priorityValue;
}
