package com.pawelnu.projectmanager.endpoints.project;

import java.util.UUID;
import lombok.Data;

@Data
public class ProjectDTO {
  private UUID id;
  private String name;
  private String category;
  private String companyName;
  private String assignedEmployee;
  private String priority;
}
