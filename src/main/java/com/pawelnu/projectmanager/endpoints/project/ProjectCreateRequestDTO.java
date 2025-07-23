package com.pawelnu.projectmanager.endpoints.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectCreateRequestDTO {
  @NotNull
  @Size(min = 5, max = 255, message = "Project name has to have 5-255 characters")
  private String name;

  @NotNull private UUID categoryValueId;
  @NotNull private UUID companyId;
  @NotNull private UUID assignedEmployeeId;
  @NotNull private UUID priorityValueId;
}
